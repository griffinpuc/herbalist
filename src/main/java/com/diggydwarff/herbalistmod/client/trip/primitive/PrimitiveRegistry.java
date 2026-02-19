package com.diggydwarff.herbalistmod.client.trip.primitive;

import com.diggydwarff.herbalistmod.client.trip.primitive.atmosphere.FogColorShiftPrimitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.camera.FovPulsePrimitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.postfx.MiragePostFxPrimitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.world.WeatherOverridePrimitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.atmosphere.ContextTintPrimitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.atmosphere.FogPulsePrimitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.camera.FovDriftPrimitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.entity.SwarmOrbitParticlesPrimitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.world.HeatHazeParticlesPrimitive;
import com.diggydwarff.herbalistmod.client.trip.primitive.world.HorizonSilhouettePrimitive;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public final class PrimitiveRegistry {
    private static final Map<String, Primitive> PRIMS = new HashMap<>();
    private static final Map<String, PrimitiveSpec> SPECS = new HashMap<>();
    private static boolean bootstrapped = false;

    public static void bootstrap() {
        if (bootstrapped) return;
        bootstrapped = true;

        register(new FogColorShiftPrimitive(),
                new PrimitiveSpec("atmo.fog_color_shift", Tags.ATMOSPHERE, false, 1.0f, EnumSet.noneOf(Tags.class)));

        register(new WeatherOverridePrimitive(),
                new PrimitiveSpec("world.weather_override", Tags.WORLD, true, 0.8f, EnumSet.noneOf(Tags.class)));

        register(new MiragePostFxPrimitive(),
                new PrimitiveSpec("postfx.mirage", Tags.POSTFX, true, 0.7f, EnumSet.of(Tags.POSTFX)));

        register(new FovPulsePrimitive(),
                new PrimitiveSpec("camera.fov_pulse", Tags.CAMERA, false, 0.9f, EnumSet.noneOf(Tags.class)));

        register(new HorizonSilhouettePrimitive(),
                new PrimitiveSpec("world.horizon_silhouette", Tags.WORLD, true, 0.55f, EnumSet.noneOf(Tags.class)));

        register(new HeatHazeParticlesPrimitive(),
                new PrimitiveSpec("world.heat_haze_particles", Tags.WORLD, false, 0.70f, EnumSet.noneOf(Tags.class)));

        register(new SwarmOrbitParticlesPrimitive(),
                new PrimitiveSpec("entity.swarm_orbit_particles", Tags.ENTITY, true, 0.50f, EnumSet.noneOf(Tags.class)));

        register(new FogPulsePrimitive(),
                new PrimitiveSpec("atmo.fog_pulse", Tags.ATMOSPHERE, false, 0.75f, EnumSet.noneOf(Tags.class)));

        register(new ContextTintPrimitive(),
                new PrimitiveSpec("atmo.context_tint", Tags.ATMOSPHERE, false, 0.60f, EnumSet.noneOf(Tags.class)));

        register(new FovDriftPrimitive(),
                new PrimitiveSpec("camera.fov_drift", Tags.CAMERA, false, 0.60f, EnumSet.noneOf(Tags.class)));

    }

    public static void register(Primitive p, PrimitiveSpec spec) {
        PRIMS.put(spec.id, p);
        SPECS.put(spec.id, spec);
    }

    public static Collection<PrimitiveSpec> specs() { return SPECS.values(); }
    public static Primitive get(String id) { return PRIMS.get(id); }
    public static PrimitiveSpec spec(String id) { return SPECS.get(id); }
}