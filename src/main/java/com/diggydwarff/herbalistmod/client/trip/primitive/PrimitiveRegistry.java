package com.diggydwarff.herbalistmod.client.trip.primitive;

import com.diggydwarff.herbalistmod.client.trip.primitive.datadriven.DataDrivenPrimitiveLoader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PrimitiveRegistry {
    private static final Map<String, Primitive> PRIMS = new HashMap<>();
    private static final Map<String, PrimitiveSpec> SPECS = new HashMap<>();
    private static boolean bootstrapped = false;

    public static void bootstrap() {
        if (bootstrapped) return;
        bootstrapped = true;

        DataDrivenPrimitiveLoader.loadAllIntoRegistry();
    }

    public static void register(Primitive prim, PrimitiveSpec spec) {
        if (prim == null || spec == null) return;
        PRIMS.put(spec.id, prim);
        SPECS.put(spec.id, spec);
    }

    public static Collection<PrimitiveSpec> specs() { return SPECS.values(); }
    public static Primitive get(String id) { return PRIMS.get(id); }
    public static PrimitiveSpec spec(String id) { return SPECS.get(id); }
}