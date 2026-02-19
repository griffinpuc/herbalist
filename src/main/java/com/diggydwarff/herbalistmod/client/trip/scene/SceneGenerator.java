package com.diggydwarff.herbalistmod.client.trip.scene;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import com.diggydwarff.herbalistmod.client.trip.rng.SeedMixer;
import com.diggydwarff.herbalistmod.client.trip.rng.SeededRng;

import java.util.*;

public final class SceneGenerator {

    // per-scene caps (tune as you add more stuff)
    private static final EnumMap<Tags, Integer> TAG_CAPS = new EnumMap<>(Tags.class);
    static {
        TAG_CAPS.put(Tags.WORLD, 2);
        TAG_CAPS.put(Tags.ATMOSPHERE, 2);
        TAG_CAPS.put(Tags.ENTITY, 1);
        TAG_CAPS.put(Tags.CAMERA, 1);
        TAG_CAPS.put(Tags.POSTFX, 1);
        TAG_CAPS.put(Tags.AUDIO, 1);
    }

    public static GeneratedScene generate(TripState state, TripContext ctx, int sceneIndex) {
        long sceneSeed = SeedMixer.sceneSeed(state.baseSeed, sceneIndex);
        SeededRng rng = new SeededRng(sceneSeed);

        int duration = rng.nextInt(20 * 20, 70 * 20);
        int fade = rng.nextInt(2 * 20, 6 * 20);

        List<PrimitiveInstance> picked = new ArrayList<>();
        EnumMap<Tags, Integer> counts = new EnumMap<>(Tags.class);

        // 1) Always 1 major
        PrimitiveSpec major = pickOne(rng, ctx, true, counts, picked);
        if (major != null) {
            add(rng, major, picked, counts);
        }

        // 2) 1–2 minors
        int minors = rng.nextInt(1, 3);
        for (int i = 0; i < minors; i++) {
            PrimitiveSpec minor = pickOne(rng, ctx, false, counts, picked);
            if (minor == null) break;
            add(rng, minor, picked, counts);
        }

        // 3) Ensure we have some "atmosphere layer" (ATMOSPHERE or POSTFX)
        boolean hasAtmo = get(counts, Tags.ATMOSPHERE) > 0;
        boolean hasPost = get(counts, Tags.POSTFX) > 0;
        if (!hasAtmo && !hasPost) {
            // Prefer atmosphere if available; otherwise postfx
            PrimitiveSpec atmo = pickSpecific("atmo.fog_pulse", "atmo.fog_color_shift", "atmo.context_tint");
            if (atmo != null && canAdd(atmo, counts, picked)) {
                add(rng, atmo, picked, counts);
            } else {
                PrimitiveSpec post = PrimitiveRegistry.spec("postfx.mirage");
                if (post != null && canAdd(post, counts, picked)) add(rng, post, picked, counts);
            }
        }

        // 4) Camera at mid/high intensity (one only)
        if (state.intensity > 0.35f && get(counts, Tags.CAMERA) < cap(Tags.CAMERA)) {
            PrimitiveSpec cam = PrimitiveRegistry.spec("camera.fov_pulse");
            if (cam != null && canAdd(cam, counts, picked)) add(rng, cam, picked, counts);
        }

        // 5) Fallback if something went wrong
        if (picked.isEmpty()) {
            PrimitiveSpec fallback = PrimitiveRegistry.specs().iterator().next();
            add(rng, fallback, picked, counts);
        }

        return new GeneratedScene(state.ticks, duration, fade, picked);
    }

    private static void add(SeededRng rng, PrimitiveSpec spec, List<PrimitiveInstance> picked, EnumMap<Tags, Integer> counts) {
        PrimitiveInstance inst = makeInstance(rng, spec);
        picked.add(inst);
        counts.put(spec.tag, get(counts, spec.tag) + 1);
    }

    private static PrimitiveInstance makeInstance(SeededRng rng, PrimitiveSpec spec) {
        Primitive p = PrimitiveRegistry.get(spec.id);
        PrimitiveInstance inst = new PrimitiveInstance(p, spec, rng.nextLong());
        inst.set("strength", rng.nextFloat(0.25f, 1.0f));
        inst.set("speed", rng.nextFloat(0.2f, 1.2f));
        return inst;
    }

    private static PrimitiveSpec pickOne(
            SeededRng rng,
            TripContext ctx,
            boolean wantMajor,
            EnumMap<Tags, Integer> counts,
            List<PrimitiveInstance> alreadyPicked
    ) {
        List<PrimitiveSpec> candidates = new ArrayList<>();
        for (PrimitiveSpec s : PrimitiveRegistry.specs()) {
            if (s.major != wantMajor) continue;
            if (!canAdd(s, counts, alreadyPicked)) continue;
            candidates.add(s);
        }
        if (candidates.isEmpty()) return null;

        float total = 0f;
        for (PrimitiveSpec c : candidates) total += Math.max(0.0001f, c.weight);

        float r = rng.nextFloat(0f, total);
        float acc = 0f;
        for (PrimitiveSpec c : candidates) {
            acc += Math.max(0.0001f, c.weight);
            if (r <= acc) return c;
        }
        return candidates.get(candidates.size() - 1);
    }

    private static boolean canAdd(PrimitiveSpec s, EnumMap<Tags, Integer> counts, List<PrimitiveInstance> picked) {
        // cap per tag
        if (get(counts, s.tag) >= cap(s.tag)) return false;

        // conflict tags: if any already-picked tag is in s.conflicts, disallow
        if (!s.conflicts.isEmpty()) {
            for (PrimitiveInstance pi : picked) {
                if (s.conflicts.contains(pi.spec.tag)) return false;
            }
        }
        return true;
    }

    private static int cap(Tags tag) {
        return TAG_CAPS.getOrDefault(tag, 1);
    }

    private static int get(EnumMap<Tags, Integer> counts, Tags tag) {
        return counts.getOrDefault(tag, 0);
    }

    private static PrimitiveSpec pickSpecific(String... ids) {
        for (String id : ids) {
            PrimitiveSpec s = PrimitiveRegistry.spec(id);
            if (s != null) return s;
        }
        return null;
    }
}
