package com.diggydwarff.herbalistmod.block.entity;
import com.diggydwarff.herbalistmod.items.ModItems;
import com.diggydwarff.herbalistmod.client.screen.ExtractionStandMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ExtractionStandEntity extends BlockEntity implements MenuProvider {


    private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    public int fuelLevel = 0;
    public int maxFuelLevel = 3000;
    private int maxProgress = 1000;

    public ExtractionStandEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXTRACTION_STAND_ENTITY.get(), pos, state);

        fuelLevel = 0;

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ExtractionStandEntity.this.progress;
                    case 1 -> ExtractionStandEntity.this.fuelLevel;
                    //case 2 -> ExtractionStandEntity.this.fuelLevel;
                    //case 3 -> ExtractionStandEntity.this.maxFuelLevel;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ExtractionStandEntity.this.progress = value;
                    case 1 -> ExtractionStandEntity.this.fuelLevel = value;
                    //case 2 -> ExtractionStandEntity.this.fuelLevel = value;
                    //case 3 -> ExtractionStandEntity.this.maxFuelLevel = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Extraction Stand");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ExtractionStandMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("extraction_stand.progress", this.progress);
        nbt.putInt("extraction_stand.maxProgress", this.maxProgress);
        nbt.putInt("extraction_stand.fuelLevel", this.fuelLevel);
        nbt.putInt("extraction_stand.maxFuelLevel", this.maxFuelLevel);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("extraction_stand.progress");
        maxProgress = nbt.getInt("extraction_stand.maxProgress");
        fuelLevel = nbt.getInt("extraction_stand.fuelLevel");
        maxFuelLevel = nbt.getInt("extraction_stand.maxFuelLevel");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ExtractionStandEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }

        if(pEntity.itemHandler.getStackInSlot(0).getItem() == Items.BLAZE_POWDER && pEntity.fuelLevel <= 0){
            pEntity.itemHandler.extractItem(0, 1, false);
            pEntity.fuelLevel = pEntity.maxFuelLevel;
        }

        if(hasRecipe(pEntity)) {

            if(pEntity.fuelLevel > 0){
                pEntity.fuelLevel--;
            }

            ServerLevel serverLevel = (ServerLevel) level;
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX(), pos.getY()+1, pos.getZ(), 1, 0, 0, 0, 0);

            pEntity.progress++;

            ItemStack itemStack = pEntity.itemHandler.getStackInSlot(1);
            itemStack.setDamageValue(itemStack.getDamageValue()+1);

            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }


    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(ExtractionStandEntity pEntity) {
        if(hasRecipe(pEntity)) {
            if(pEntity.itemHandler.getStackInSlot(1).getItem() == ModItems.ETHEREAL_FUNGUS.get() ||
                    pEntity.itemHandler.getStackInSlot(1).getItem() == ModItems.DREAMCAP_MUSHROOM.get()||
                    pEntity.itemHandler.getStackInSlot(1).getItem() == ModItems.SNOWCAP_MUSHROOM.get() ||
                    pEntity.itemHandler.getStackInSlot(1).getItem() == ModItems.GOLDENGLOW_MUSHROOM.get()){
                if(pEntity.itemHandler.getStackInSlot(2).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(2, 1, false);
                    pEntity.itemHandler.insertItem(2, new ItemStack(ModItems.MAGIC_FUNGUS_EXTRACT.get()), false);
                }
                if(pEntity.itemHandler.getStackInSlot(3).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(3, 1, false);
                    pEntity.itemHandler.insertItem(3, new ItemStack(ModItems.MAGIC_FUNGUS_EXTRACT.get()), false);
                }
                if(pEntity.itemHandler.getStackInSlot(4).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(4, 1, false);
                    pEntity.itemHandler.insertItem(4, new ItemStack(ModItems.MAGIC_FUNGUS_EXTRACT.get()), false);
                }
            } else if(pEntity.itemHandler.getStackInSlot(1).getItem() == ModItems.MIRAGE_CACTUS.get()){
                if(pEntity.itemHandler.getStackInSlot(2).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(2, 1, false);
                    pEntity.itemHandler.insertItem(2, new ItemStack(ModItems.MIRAGE_CACTUS_EXTRACT.get()), false);
                }
                if(pEntity.itemHandler.getStackInSlot(3).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(3, 1, false);
                    pEntity.itemHandler.insertItem(3, new ItemStack(ModItems.MIRAGE_CACTUS_EXTRACT.get()), false);
                }
                if(pEntity.itemHandler.getStackInSlot(4).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(4, 1, false);
                    pEntity.itemHandler.insertItem(4, new ItemStack(ModItems.MIRAGE_CACTUS_EXTRACT.get()), false);
                }
            } else if(pEntity.itemHandler.getStackInSlot(1).getItem() == Items.POISONOUS_POTATO){
                if(pEntity.itemHandler.getStackInSlot(2).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(2, 1, false);
                    pEntity.itemHandler.insertItem(2, new ItemStack(ModItems.SUSPICIOUS_EXTRACT.get()), false);
                }
                if(pEntity.itemHandler.getStackInSlot(3).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(3, 1, false);
                    pEntity.itemHandler.insertItem(3, new ItemStack(ModItems.SUSPICIOUS_EXTRACT.get()), false);
                }
                if(pEntity.itemHandler.getStackInSlot(4).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(4, 1, false);
                    pEntity.itemHandler.insertItem(4, new ItemStack(ModItems.SUSPICIOUS_EXTRACT.get()), false);
                }
            } else if(pEntity.itemHandler.getStackInSlot(1).getItem() == Items.CHORUS_FRUIT){
                if(pEntity.itemHandler.getStackInSlot(2).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(2, 1, false);
                    pEntity.itemHandler.insertItem(2, new ItemStack(ModItems.CHORUS_EXTRACT.get()), false);
                }
                if(pEntity.itemHandler.getStackInSlot(3).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(3, 1, false);
                    pEntity.itemHandler.insertItem(3, new ItemStack(ModItems.CHORUS_EXTRACT.get()), false);
                }
                if(pEntity.itemHandler.getStackInSlot(4).getItem() == ModItems.GLASS_VIAL.get()){
                    pEntity.itemHandler.extractItem(4, 1, false);
                    pEntity.itemHandler.insertItem(4, new ItemStack(ModItems.CHORUS_EXTRACT.get()), false);
                }
            }

            pEntity.itemHandler.extractItem(1, 1, false);
        }
    }

    private static boolean hasRecipe(ExtractionStandEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        List<Item> possibleIngredientItems = Arrays.asList(new Item[]{
                ModItems.MIRAGE_CACTUS.get(),
                ModItems.ETHEREAL_FUNGUS.get(),
                ModItems.DREAMCAP_MUSHROOM.get(),
                ModItems.SNOWCAP_MUSHROOM.get(),
                ModItems.GOLDENGLOW_MUSHROOM.get(),
                Items.CHORUS_FRUIT,
                Items.POISONOUS_POTATO
        });

        boolean hasFuel = entity.fuelLevel >= 0;
        boolean hasIngredient = possibleIngredientItems.contains(entity.itemHandler.getStackInSlot(1).getItem());
        boolean hasVials = ((entity.itemHandler.getStackInSlot(2).getItem() == ModItems.GLASS_VIAL.get()) || (entity.itemHandler.getStackInSlot(3).getItem() == ModItems.GLASS_VIAL.get()) || (entity.itemHandler.getStackInSlot(4).getItem() == ModItems.GLASS_VIAL.get()));

        return hasFuel && hasVials && hasIngredient;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }
    
}
