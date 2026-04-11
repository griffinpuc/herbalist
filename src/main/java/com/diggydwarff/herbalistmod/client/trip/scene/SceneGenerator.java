package com.diggydwarff.herbalistmod.client.trip.scene;

import com.diggydwarff.herbalistmod.client.trip.TripContext;
import com.diggydwarff.herbalistmod.client.trip.TripState;
import com.diggydwarff.herbalistmod.client.trip.primitive.*;
import com.diggydwarff.herbalistmod.client.trip.rng.SeedMixer;
import com.diggydwarff.herbalistmod.client.trip.rng.SeededRng;

import java.util.*;

public final class SceneGenerator {

    // Base caps; final caps scale up with intensity.
    private static final EnumMap<Tags, Integer> BASE_TAG_CAPS = new EnumMap<>(Tags.class);
    static {
        BASE_TAG_CAPS.put(Tags.WORLD, 2);
        BASE_TAG_CAPS.put(Tags.ATMOSPHERE, 2);
        BASE_TAG_CAPS.put(Tags.ENTITY, 1);
        BASE_TAG_CAPS.put(Tags.CAMERA, 1);
        BASE_TAG_CAPS.put(Tags.POSTFX, 1);
        BASE_TAG_CAPS.put(Tags.AUDIO, 1);
    }

    public static GeneratedScene generateSingle(
            TripState state,
            TripContext ctx,
            int sceneIndex,
            String forcedPrimitiveId,
            int durationTicks
    ) {
        long sceneSeed = SeedMixer.sceneSeed(state.baseSeed, sceneIndex);
        SeededRng rng = new SeededRng(sceneSeed);

        PrimitiveSpec spec = PrimitiveRegistry.spec(forcedPrimitiveId);
        if (spec == null) {
            // fallback to normal generation if id is bad
            return generate(state, ctx, sceneIndex);
        }

        int fade = Math.min(6 * 20, Math.max(10, durationTicks / 6));

        Primitive p = PrimitiveRegistry.get(spec.id);
        PrimitiveInstance inst = new PrimitiveInstance(p, spec, rng.nextLong());
        inst.set("strength", 1.0f);
        inst.set("speed", 1.0f);

        state.notePrimitive(spec.id);

        return new GeneratedScene(state.ticks, durationTicks, fade, java.util.List.of(inst));
    }

    public static GeneratedScene generate(TripState state, TripContext ctx, int sceneIndex) {
        long sceneSeed = SeedMixer.sceneSeed(state.baseSeed, sceneIndex);
        SeededRng rng = new SeededRng(sceneSeed);

        // Longer scenes at higher intensity
        int duration = rng.nextInt(20 * 18, 70 * 20) + (int)(state.intensity * 20 * 10);
        int fade = rng.nextInt(2 * 20, 6 * 20);

        List<PrimitiveInstance> picked = new ArrayList<>();
        EnumMap<Tags, Integer> counts = new EnumMap<>(Tags.class);

        // Go harder: more layers, scaled by intensity.
        int majors = lerpInt(2, 4, state.intensity);      // 2..4
        int minors = lerpInt(4, 10, state.intensity);     // 4..10

        // 1) Pick majors (ACTUALLY majors)
        for (int i = 0; i < majors; i++) {
            PrimitiveSpec major = pickOne(rng, state, true, counts, picked);
            if (major == null) break;
            add(rng, state, major, picked, counts);
        }

        // 2) Pick minors
        for (int i = 0; i < minors; i++) {
            PrimitiveSpec minor = pickOne(rng, state, false, counts, picked);
            if (minor == null) break;
            add(rng, state, minor, picked, counts);
        }

        // 3) Ensure we have some atmosphere layer (ATMOSPHERE or POSTFX)
        boolean hasAtmo = get(counts, Tags.ATMOSPHERE) > 0;
        boolean hasPost = get(counts, Tags.POSTFX) > 0;
        if (!hasAtmo && !hasPost) {
            PrimitiveSpec atmo = pickSpecific("atmo.fog_pulse", "atmo.fog_color_shift", "atmo.context_tint");
            if (atmo != null && canAdd(state, atmo, counts, picked)) {
                add(rng, state, atmo, picked, counts);
            } else {
                PrimitiveSpec post = PrimitiveRegistry.spec("postfx.mirage");
                if (post != null && canAdd(state, post, counts, picked)) {
                    add(rng, state, post, picked, counts);
                }
            }
        }

        // 4) Guarantee some WORLD presence at moderate intensity
        if (state.intensity > 0.25f && get(counts, Tags.WORLD) == 0) {
            PrimitiveSpec world = pickOneOfTag(rng, state, Tags.WORLD, counts, picked);
            if (world != null) add(rng, state, world, picked, counts);
        }

        // Fallback
        if (picked.isEmpty()) {
            Collection<PrimitiveSpec> specs = PrimitiveRegistry.specs();
            if (specs == null || specs.isEmpty()) {
                System.out.println("[Trip] ERROR: PrimitiveRegistry is empty (no primitives loaded).");
                return new GeneratedScene(state.ticks, 40, 10, List.of());
            }
            PrimitiveSpec fallback = specs.iterator().next();
            picked.add(makeInstance(rng, state, fallback));
            state.notePrimitive(fallback.id);
        }

        return new GeneratedScene(state.ticks, duration, fade, picked);
    }

    private static void add(SeededRng rng, TripState state, PrimitiveSpec spec,
                            List<PrimitiveInstance> picked, EnumMap<Tags, Integer> counts) {
        PrimitiveInstance inst = makeInstance(rng, state, spec);
        picked.add(inst);
        counts.put(spec.tag, get(counts, spec.tag) + 1);
        state.notePrimitive(spec.id);
    }

    private static PrimitiveInstance makeInstance(SeededRng rng, TripState state, PrimitiveSpec spec) {
        Primitive p = PrimitiveRegistry.get(spec.id);
        PrimitiveInstance inst = new PrimitiveInstance(p, spec, rng.nextLong());

        // Make it noticeable: bias strength upward with intensity.
        float baseStrength = rng.nextFloat(0.35f, 1.0f);
        float intensityBoost = 0.55f + 0.45f * clamp01(state.intensity);
        inst.set("strength", clamp01(baseStrength * intensityBoost * state.profile.strengthMult));

        // Speed slightly biased up at higher intensity.
        float baseSpeed = rng.nextFloat(0.25f, 1.25f);
        float speedBoost = 0.80f + 0.40f * clamp01(state.intensity);
        inst.set("speed", clamp01(baseSpeed * speedBoost * state.profile.speedMult));

        return inst;
    }

    private static PrimitiveSpec pickOne(
            SeededRng rng,
            TripState state,
            boolean wantMajor,
            EnumMap<Tags, Integer> counts,
            List<PrimitiveInstance> alreadyPicked
    ) {
        // Build candidates
        List<PrimitiveSpec> candidates = new ArrayList<>();
        for (PrimitiveSpec s : PrimitiveRegistry.specs()) {
            if (s.major != wantMajor) continue;
            if (!canAdd(state, s, counts, alreadyPicked)) continue;
            candidates.add(s);
        }
        if (candidates.isEmpty()) return null;

        // Weighted pick
        float total = 0f;
        for (PrimitiveSpec c : candidates) total += weightFor(rng, state, c);

        // Try a few rolls to avoid "recent" repeats
        for (int tries = 0; tries < 6; tries++) {
            float r = rng.nextFloat(0f, total);
            float acc = 0f;
            for (PrimitiveSpec c : candidates) {
                acc += weightFor(rng, state, c);
                if (r <= acc) {
                    if (!state.recentlyUsed(c.id)) return c;
                    break; // reroll
                }
            }
        }

        // If all are recent, just return a weighted result
        float r = rng.nextFloat(0f, total);
        float acc = 0f;
        for (PrimitiveSpec c : candidates) {
            acc += weightFor(rng, state, c);
            if (r <= acc) return c;
        }
        return candidates.get(candidates.size() - 1);
    }

    private static PrimitiveSpec pickOneOfTag(
            SeededRng rng,
            TripState state,
            Tags tag,
            EnumMap<Tags, Integer> counts,
            List<PrimitiveInstance> alreadyPicked
    ) {
        List<PrimitiveSpec> candidates = new ArrayList<>();
        for (PrimitiveSpec s : PrimitiveRegistry.specs()) {
            if (s.tag != tag) continue;
            if (!canAdd(state, s, counts, alreadyPicked)) continue;
            candidates.add(s);
        }
        if (candidates.isEmpty()) return null;

        float total = 0f;
        for (PrimitiveSpec c : candidates) total += weightFor(rng, state, c);

        float r = rng.nextFloat(0f, total);
        float acc = 0f;
        for (PrimitiveSpec c : candidates) {
            acc += weightFor(rng, state, c);
            if (r <= acc) return c;
        }
        return candidates.get(candidates.size() - 1);
    }

    private static boolean canAdd(TripState state, PrimitiveSpec s,
                                  EnumMap<Tags, Integer> counts,
                                  List<PrimitiveInstance> picked) {
        // Avoid exact duplicates in the same scene
        for (PrimitiveInstance pi : picked) {
            if (pi.spec != null && s.id.equals(pi.spec.id)) return false;
        }

        // Cap per tag (scaled by intensity)
        if (get(counts, s.tag) >= cap(state, s.tag)) return false;

        // Respect conflicts
        if (!s.conflicts.isEmpty()) {
            for (PrimitiveInstance pi : picked) {
                if (pi.spec != null && s.conflicts.contains(pi.spec.tag)) return false;
            }
        }
        return true;
    }

    private static int cap(TripState state, Tags tag) {
        int base = BASE_TAG_CAPS.getOrDefault(tag, 1);

        // Scale caps with intensity so more layers can stack
        float t = clamp01(state.intensity);
        int bonus;
        switch (tag) {
            case WORLD -> bonus = lerpInt(1, 4, t);        // +1..+4
            case ATMOSPHERE -> bonus = lerpInt(1, 2, t);   // +1..+2
            case CAMERA -> bonus = (t > 0.35f) ? 1 : 0;    // allow 2 at higher intensity
            case ENTITY -> bonus = (t > 0.45f) ? 1 : 0;
            case AUDIO -> bonus = (t > 0.50f) ? 1 : 0;
            default -> bonus = 0;
        }
        return base + bonus;
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

    private static int lerpInt(int a, int b, float t) {
        t = clamp01(t);
        return a + Math.round((b - a) * t);
    }

    private static float clamp01(float v) {
        return v < 0f ? 0f : Math.min(v, 1f);
    }

    private static float weightFor(SeededRng rng, TripState state, PrimitiveSpec spec) {
        float w = Math.max(0.0001f, spec.weight);

        // Profile multipliers
        w *= state.profile.multFor(spec.tag);
        w *= state.profile.multFor(spec.id);

        // Phase bias
        float p = clamp01(state.progress);
        if (p < 0.33f) w *= state.profile.earlyBias;
        else if (p < 0.66f) w *= state.profile.peakBias;
        else w *= state.profile.lateBias;

        // Chaos jitter (deterministic via rng)
        float chaos = clamp01(state.profile.chaos);
        if (chaos > 0.0001f) {
            float jitter = 1.0f + (rng.nextFloat(-0.5f, 0.5f) * chaos);
            w *= Math.max(0.05f, jitter);
        }

        return Math.max(0.0001f, w);
    }

}