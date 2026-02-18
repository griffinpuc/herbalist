package com.diggydwarff.herbalistmod.villager;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.block.ModBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

public class ModVillagers {

    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, HerbalistMod.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, HerbalistMod.MODID);

    public static final RegistryObject<PoiType> EXTRACTION_STAND_POI = POI_TYPES.register("extraction_stand_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.EXTRACTION_STAND_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> HERBALIST_MASTER =
            VILLAGER_PROFESSIONS.register("herbalist_master", () ->
                    new VillagerProfession(
                            "herbalist_master",
                            holder -> holder.is(EXTRACTION_STAND_POI.getKey()),
                            holder -> holder.is(EXTRACTION_STAND_POI.getKey()),
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_ARMORER
                    )
            );


    public static void registerPOIs() {
        try {
            Holder<?> holder = EXTRACTION_STAND_POI.getHolder().orElseThrow();
            Set<BlockState> states = ImmutableSet.copyOf(
                    ModBlocks.EXTRACTION_STAND_BLOCK.get().getStateDefinition().getPossibleStates()
            );

            // Don't rely on the name (it may be obfuscated). Find the static method that takes (Holder, Set).
            Method m = Arrays.stream(PoiTypes.class.getDeclaredMethods())
                    .filter(mm -> Modifier.isStatic(mm.getModifiers()))
                    .filter(mm -> mm.getParameterCount() == 2)
                    .filter(mm -> Holder.class.isAssignableFrom(mm.getParameterTypes()[0]))
                    .filter(mm -> Set.class.isAssignableFrom(mm.getParameterTypes()[1]))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchMethodException("PoiTypes.*(Holder, Set) not found"));

            m.setAccessible(true);
            m.invoke(null, holder, states);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}