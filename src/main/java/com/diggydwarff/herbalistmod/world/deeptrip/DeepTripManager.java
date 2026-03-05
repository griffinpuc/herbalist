package com.diggydwarff.herbalistmod.world.deeptrip;

import com.diggydwarff.herbalistmod.network.PacketHandeler;
import com.diggydwarff.herbalistmod.network.TripTransitionS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

public final class DeepTripManager {
    private static final String TAG_ROOT = "herbalistmod_deeptrip";
    private static final int REGION_SPACING = 1024;
    private static final String TAG_TRANSITIONING = "deepTripTransitioning";

    private static boolean isTransitioning(ServerPlayer p) {
        return p.getPersistentData().getBoolean(TAG_TRANSITIONING);
    }
    private static void setTransitioning(ServerPlayer p, boolean v) {
        p.getPersistentData().putBoolean(TAG_TRANSITIONING, v);
    }

    private DeepTripManager() {}

    public static boolean isActive(ServerPlayer player) {
        return player.getPersistentData().contains(TAG_ROOT);
    }

    public static void enterInternal(ServerPlayer player) {
        if (isActive(player)) return;

        MinecraftServer server = player.server;
        ServerLevel overworld = server.overworld();
        ServerLevel trip = server.getLevel(TripDimensions.TRIP_LEVEL);
        if (trip == null) return;

        // Save return location
        CompoundTag root = new CompoundTag();
        root.putString("returnDim", player.level().dimension().location().toString());
        root.putLong("returnPos", player.blockPosition().asLong());
        root.putFloat("returnYaw", player.getYRot());
        root.putFloat("returnPitch", player.getXRot());

        // Spawn dummy "body" (cheap first version)
        ArmorStand stand = new ArmorStand(player.level(), player.getX(), player.getY(), player.getZ());
        stand.setNoGravity(true);
        stand.setInvulnerable(true);
        stand.setCustomName(player.getDisplayName());
        stand.setCustomNameVisible(true);

        copyEquipment(player, stand);
        player.level().addFreshEntity(stand);

        root.putUUID("bodyUuid", stand.getUUID());

        // Allocate a unique region inside the Trip dimension
        int region = DeepTripSavedData.get(overworld).allocateRegion();
        int baseX = region * REGION_SPACING;
        int baseZ = 0;

        // after baseX/baseZ are known and trip level is non-null
        trip.getWorldBorder().setCenter(baseX + 0.5, baseZ + 0.5);
        trip.getWorldBorder().setSize(128.0);

        long seed = RandomSource.create().nextLong();
        int radius = 75;
        int baseY = 80;

        root.putLong("seed", seed);
        root.putInt("baseX", baseX);
        root.putInt("baseZ", baseZ);

        trip.getWorldBorder().setCenter(baseX + 0.5, baseZ + 0.5);
        trip.getWorldBorder().setSize((radius * 2) + 16); // a little buffer

        BlockPos center = new BlockPos(baseX, baseY, baseZ);

        trip.getWorldBorder().setCenter(baseX + 0.5, baseZ + 0.5);
        trip.getWorldBorder().setSize((radius * 2) + 24);

        TripProfile profile = TripProfile.fromSeed(seed);

        TripTerrainBuilder.start(player.getUUID(), trip, center, profile, radius, baseY);

        // landing target (center)
        root.putInt("landingY", baseY + 2);
        root.putInt("landingX", baseX);
        root.putInt("landingZ", baseZ);
        root.putBoolean("dropping", true);
        player.getPersistentData().put(TAG_ROOT, root);

        // teleport high
        double startY = baseY + 20;
        player.teleportTo(trip, baseX + 0.5, startY, baseZ + 0.5, player.getYRot(), player.getXRot());
    }

    public static void requestEnter(ServerPlayer player) {
        if (isActive(player)) return;
        if (isTransitioning(player)) return;
        setTransitioning(player, true);

        long seed = computeSeedForPlayer(player);
        long sessionId = seed;

        PacketHandeler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new TripTransitionS2CPacket(true, 12, 30, 15, true, seed, sessionId));

        DeepTripTransitionQueue.scheduleEnter(player, 14);
        setTransitioning(player, false);
    }

    public static void requestExit(ServerPlayer player) {
        if (!isActive(player)) return;
        if (isTransitioning(player)) return;
        setTransitioning(player, true);

        PacketHandeler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                new TripTransitionS2CPacket(false, 10, 12, 10, true, 0L, 0L));

        DeepTripTransitionQueue.scheduleExit(player, 10);
        setTransitioning(player, false);
    }

    private static long computeSeedForPlayer(ServerPlayer player) {
        long w = player.serverLevel().getSeed();
        long u = player.getUUID().getMostSignificantBits();
        long v = player.getUUID().getLeastSignificantBits();
        return w ^ u ^ (v * 31L);
    }

    public static void exitInternal(ServerPlayer player) {
        CompoundTag pd = player.getPersistentData();
        if (!pd.contains(TAG_ROOT)) return;

        CompoundTag root = pd.getCompound(TAG_ROOT);

        // Remove dummy body
        if (root.hasUUID("bodyUuid")) {
            UUID id = root.getUUID("bodyUuid");
            ServerLevel cur = player.serverLevel();
            Entity e = cur.getEntity(id);
            if (e != null) e.discard();
            else {
                for (ServerLevel lvl : player.server.getAllLevels()) {
                    Entity e2 = lvl.getEntity(id);
                    if (e2 != null) { e2.discard(); break; }
                }
            }
        }

        // Teleport back
        ResourceKey<Level> returnKey = ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
                new net.minecraft.resources.ResourceLocation(root.getString("returnDim")));
        ServerLevel returnLevel = player.server.getLevel(returnKey);
        if (returnLevel == null) returnLevel = player.server.overworld();

        BlockPos returnPos = BlockPos.of(root.getLong("returnPos"));
        float yaw = root.getFloat("returnYaw");
        float pitch = root.getFloat("returnPitch");

        player.teleportTo(returnLevel, returnPos.getX() + 0.5, returnPos.getY(), returnPos.getZ() + 0.5, yaw, pitch);

        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.onUpdateAbilities();
        player.setInvulnerable(false);

        // Clear state
        pd.remove(TAG_ROOT);
    }

    private static void copyEquipment(ServerPlayer player, ArmorStand stand) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack s = player.getItemBySlot(slot);
            if (!s.isEmpty()) stand.setItemSlot(slot, s.copy());
        }
        stand.setYRot(player.getYRot());
        stand.setXRot(0);
    }

    private static void buildPreview(ServerLevel level, BlockPos origin, long seed) {
        RandomSource r = RandomSource.create(seed);

        // Small platform
        for (int x = -6; x <= 6; x++) {
            for (int z = -6; z <= 6; z++) {
                level.setBlock(origin.offset(x, 0, z), Blocks.SMOOTH_STONE.defaultBlockState(), 3);
            }
        }
        level.setBlock(origin.offset(0, 1, 0), Blocks.GLOWSTONE.defaultBlockState(), 3);

        // A “mosaic floor” out to 64x64
        BlockPos floor = origin.offset(-32, -1, -32);
        for (int x = 0; x < 64; x++) {
            for (int z = 0; z < 64; z++) {
                var state = pickTripBlock(r);
                level.setBlock(floor.offset(x, 0, z), state, 3);
            }
        }

        // A few random pillars
        for (int i = 0; i < 24; i++) {
            int px = origin.getX() + r.nextInt(-28, 29);
            int pz = origin.getZ() + r.nextInt(-28, 29);
            int h = r.nextInt(4, 18);
            for (int y = 0; y < h; y++) {
                level.setBlock(new BlockPos(px, origin.getY() + y, pz), pickTripBlock(r), 3);
            }
        }
    }

    private static net.minecraft.world.level.block.state.BlockState pickTripBlock(RandomSource r) {
        // Cheap “everything is different” starter palette
        return switch (r.nextInt(10)) {
            case 0 -> Blocks.BLUE_STAINED_GLASS.defaultBlockState();
            case 1 -> Blocks.PINK_STAINED_GLASS.defaultBlockState();
            case 2 -> Blocks.LIME_CONCRETE.defaultBlockState();
            case 3 -> Blocks.PURPLE_CONCRETE.defaultBlockState();
            case 4 -> Blocks.CYAN_GLAZED_TERRACOTTA.defaultBlockState();
            case 5 -> Blocks.MAGENTA_GLAZED_TERRACOTTA.defaultBlockState();
            case 6 -> Blocks.SEA_LANTERN.defaultBlockState();
            case 7 -> Blocks.AMETHYST_BLOCK.defaultBlockState();
            case 8 -> Blocks.QUARTZ_BLOCK.defaultBlockState();
            default -> Blocks.OBSIDIAN.defaultBlockState();
        };
    }
}