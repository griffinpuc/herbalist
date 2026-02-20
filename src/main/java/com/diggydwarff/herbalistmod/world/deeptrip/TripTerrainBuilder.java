package com.diggydwarff.herbalistmod.world.deeptrip;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TripTerrainBuilder {

    private static final Map<UUID, Job> JOBS = new HashMap<>();

    // Higher = faster generation, more spikes.
    private static final int COLUMNS_PER_TICK = 1800;

    private TripTerrainBuilder() {}

    public static void start(UUID sessionKey,
                             ServerLevel level,
                             BlockPos center,
                             long seed,
                             int radius,
                             int baseY,
                             TripTheme theme) {
        JOBS.put(sessionKey, new Job(level, center, seed, radius, baseY, theme));

        // Ensure center exists immediately
        buildOneColumn(level, center.getX(), center.getZ(), center, seed, radius, baseY, theme);
    }

    public static boolean isRunning(UUID sessionKey) {
        return JOBS.containsKey(sessionKey);
    }

    public static void tick() {
        if (JOBS.isEmpty()) return;

        for (var it = JOBS.entrySet().iterator(); it.hasNext(); ) {
            Job job = it.next().getValue();

            int done = 0;
            while (done < COLUMNS_PER_TICK && job.hasNext()) {
                int x = job.nextX();
                int z = job.nextZ();

                buildOneColumn(job.level, x, z, job.center, job.seed, job.radius, job.baseY, job.theme);

                job.advance();
                done++;
            }

            if (!job.hasNext()) {
                // one-time phase after terrain completes
                decorate(job);
                spawnMobs(job);
                it.remove();
            }
        }
    }

    private static void buildOneColumn(ServerLevel level,
                                       int x, int z,
                                       BlockPos center,
                                       long seed,
                                       int radius,
                                       int baseY,
                                       TripTheme theme) {

        int dx = x - center.getX();
        int dz = z - center.getZ();

        float dist = Mth.sqrt(dx * dx + dz * dz);
        float d = Mth.clamp(dist / (float) radius, 0f, 1f);

        // Real seeded noise 0..1
        float n = TripNoise.fbm(seed, x, z);

        // Hills
        float hills = (n - 0.5f) * 36.0f; // +-18

        // Valley: lower near center
        float valley = (1f - d) * 12.0f;

        // Rim wall near border
        float inner = 0.72f;
        float t = Mth.clamp((d - inner) / (1f - inner), 0f, 1f);
        float wall = smoothstep(t) * 42.0f;

        int height = baseY + Math.round(hills - valley + wall);
        height = Mth.clamp(height, baseY - 12, baseY + 72);

        // Force chunk load
        level.getChunk(x >> 4, z >> 4);

        int minY = baseY - 20;

        for (int y = minY; y <= height; y++) {
            int depth = height - y;

            BlockState state;
            if (depth == 0) state = theme.surface();
            else if (depth <= 4) state = theme.subsurface();
            else state = theme.stone();

            level.setBlock(new BlockPos(x, y, z), state, 2);
        }
    }

    private static void decorate(Job job) {
        RandomSource r = RandomSource.create(job.seed ^ 9999L);

        // Keep this moderate; it’s one-time and can spike if huge.
        int attempts = job.radius * 3;

        for (int i = 0; i < attempts; i++) {
            int x = job.center.getX() + r.nextInt(-job.radius, job.radius + 1);
            int z = job.center.getZ() + r.nextInt(-job.radius, job.radius + 1);

            // Stay inside circle
            int dx = x - job.center.getX();
            int dz = z - job.center.getZ();
            if (dx * dx + dz * dz > job.radius * job.radius) continue;

            int y = findTop(job.level, x, z, job.baseY + 120);

            if (r.nextFloat() < job.theme.weirdness()) {
                // Accent spikes
                int h = r.nextInt(3, 9);
                for (int j = 0; j < h; j++) {
                    job.level.setBlock(new BlockPos(x, y + j, z), job.theme.accent(), 2);
                }
            } else {
                // Simple tree. Later: theme-specific trees.
                int trunk = r.nextInt(3, 6);
                for (int j = 0; j < trunk; j++) {
                    job.level.setBlock(new BlockPos(x, y + j, z), Blocks.OAK_LOG.defaultBlockState(), 2);
                }
                job.level.setBlock(new BlockPos(x, y + trunk, z), Blocks.OAK_LEAVES.defaultBlockState(), 2);
            }
        }
    }

    private static void spawnMobs(Job job) {
        RandomSource r = RandomSource.create(job.seed ^ 5555L);

        for (var entry : job.theme.mobs()) {
            int count = r.nextInt(entry.minCount(), entry.maxCount() + 1);

            for (int i = 0; i < count; i++) {
                int x = job.center.getX() + r.nextInt(-job.radius, job.radius + 1);
                int z = job.center.getZ() + r.nextInt(-job.radius, job.radius + 1);

                int dx = x - job.center.getX();
                int dz = z - job.center.getZ();
                if (dx * dx + dz * dz > job.radius * job.radius) continue;

                int y = findTop(job.level, x, z, job.baseY + 120);

                Entity e = entry.type().create(job.level);
                if (e == null) continue;

                e.moveTo(x + 0.5, y, z + 0.5, r.nextFloat() * 360f, 0);

                if (e instanceof Sheep sheep && entry.sheepColor() != null) {
                    sheep.setColor(entry.sheepColor());
                }

                job.level.addFreshEntity(e);
            }
        }
    }

    private static int findTop(ServerLevel level, int x, int z, int startY) {
        for (int y = startY; y > -64; y--) {
            if (!level.getBlockState(new BlockPos(x, y, z)).isAir()) {
                return y + 1;
            }
        }
        return startY;
    }

    private static float smoothstep(float t) {
        return t * t * (3f - 2f * t);
    }

    private static final class Job {
        final ServerLevel level;
        final BlockPos center;
        final long seed;
        final int radius;
        final int baseY;
        final TripTheme theme;

        private int x;
        private int z;
        private final int minX, maxX, minZ, maxZ;

        Job(ServerLevel level, BlockPos center, long seed, int radius, int baseY, TripTheme theme) {
            this.level = level;
            this.center = center;
            this.seed = seed;
            this.radius = radius;
            this.baseY = baseY;
            this.theme = theme;

            this.minX = center.getX() - radius;
            this.maxX = center.getX() + radius;
            this.minZ = center.getZ() - radius;
            this.maxZ = center.getZ() + radius;

            this.x = minX;
            this.z = minZ;
        }

        boolean hasNext() { return z <= maxZ; }
        int nextX() { return x; }
        int nextZ() { return z; }

        void advance() {
            x++;
            if (x > maxX) {
                x = minX;
                z++;
            }
        }
    }
}