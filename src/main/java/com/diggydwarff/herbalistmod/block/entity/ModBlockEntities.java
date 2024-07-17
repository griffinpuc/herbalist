package com.diggydwarff.herbalistmod.block.entity;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HerbalistMod.MODID);

    public static final RegistryObject<BlockEntityType<ExtractionStandEntity>> EXTRACTION_STAND_ENTITY = BLOCK_ENTITIES.register("extraction_stand_block_entity", () ->
            BlockEntityType.Builder.of(ExtractionStandEntity::new, ModBlocks.EXTRACTION_STAND_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
