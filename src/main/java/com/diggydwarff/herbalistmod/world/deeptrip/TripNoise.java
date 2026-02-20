package com.diggydwarff.herbalistmod.world.deeptrip;

import net.minecraft.util.Mth;

public final class TripNoise {

    private TripNoise() {}

    // Public entry point
    public static float fbm(long seed, float x, float z) {
        float amplitude = 1f;
        float frequency = 0.0055f;   // large hills
        float sum = 0f;
        float norm = 0f;

        for (int i = 0; i < 4; i++) {
            sum += amplitude * valueNoise(seed + i * 1013L, x * frequency, z * frequency);
            norm += amplitude;

            amplitude *= 0.5f;
            frequency *= 2.0f;
        }

        return sum / norm; // ~0..1
    }

    // Base 2D value noise
    private static float valueNoise(long seed, float x, float z) {
        int x0 = Mth.floor(x);
        int z0 = Mth.floor(z);
        int x1 = x0 + 1;
        int z1 = z0 + 1;

        float tx = x - x0;
        float tz = z - z0;

        float v00 = hash01(seed, x0, z0);
        float v10 = hash01(seed, x1, z0);
        float v01 = hash01(seed, x0, z1);
        float v11 = hash01(seed, x1, z1);

        float a = lerp(v00, v10, smooth(tx));
        float b = lerp(v01, v11, smooth(tx));
        return lerp(a, b, smooth(tz));
    }

    private static float hash01(long seed, int x, int z) {
        long h = seed ^ (x * 341873128712L) ^ (z * 132897987541L);
        h ^= (h >>> 33);
        h *= 0xff51afd7ed558ccdL;
        h ^= (h >>> 33);
        return (h & 0xFFFFFF) / (float) 0x1000000;
    }

    private static float smooth(float t) {
        return t * t * (3f - 2f * t);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}