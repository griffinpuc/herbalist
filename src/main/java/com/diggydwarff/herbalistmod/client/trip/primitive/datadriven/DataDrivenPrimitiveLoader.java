package com.diggydwarff.herbalistmod.client.trip.primitive.datadriven;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class DataDrivenPrimitiveLoader {
    private static final Gson GSON = new GsonBuilder().create();

    private DataDrivenPrimitiveLoader() {}

    public static void loadAllIntoRegistry() {

        ResourceManager rm = Minecraft.getInstance().getResourceManager();
        Map<ResourceLocation, Resource> found = rm.listResources("trip/primitives", rl -> rl.getPath().endsWith(".json"));
        System.out.println("[Trip] listResources found=" + found.size());

        int ok = 0, bad = 0;

        for (var entry : found.entrySet()) {
            ResourceLocation rl = entry.getKey();
            Resource res = entry.getValue();

            try (var in = res.open();
                 var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {

                PrimitiveDef def = GSON.fromJson(reader, PrimitiveDef.class);
                if (def == null || def.id == null || def.type == null || def.tag == null) {
                    bad++;
                    System.out.println("[Trip] BAD DEF " + rl + " (missing fields)");
                    continue;
                }

                Primitive prim = switch (def.type) {
                    case "fog" -> new DataDrivenFogPrimitive(def);
                    case "fov" -> new DataDrivenFovPrimitive(def);
                    case "particles" -> new DataDrivenParticlePrimitive(def);
                    case "entity_particles" -> new DataDrivenEntityParticlePrimitive(def);
                    case "audio" -> new DataDrivenAudioPrimitive(def);
                    default -> null;
                };

                if (prim == null) {
                    bad++;
                    System.out.println("[Trip] BAD TYPE " + rl + " type=" + def.type);
                    continue;
                }

                EnumSet<Tags> conflicts = EnumSet.noneOf(Tags.class);
                if (def.conflicts != null) conflicts.addAll(def.conflicts);

                PrimitiveRegistry.register(prim, new PrimitiveSpec(def.id, def.tag, def.major, def.weight, conflicts));
                ok++;

            } catch (Throwable t) {
                bad++;
                System.out.println("[Trip] Failed loading " + rl + ": " + t);
            }
        }

        System.out.println("[Trip] Loaded datadriven primitives ok=" + ok + " bad=" + bad);
        System.out.println("[Trip] Registry size now=" + PrimitiveRegistry.specs().size());

    }
}