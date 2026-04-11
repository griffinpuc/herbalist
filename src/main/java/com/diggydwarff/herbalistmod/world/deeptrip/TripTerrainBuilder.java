package com.diggydwarff.herbalistmod.world.deeptrip;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TripTerrainBuilder {

    private static final Map<UUID, Job> JOBS = new HashMap<>();
    private static final int COLUMNS_PER_TICK = 1800;

    private TripTerrainBuilder() {}

    public static void start(UUID sessionKey,
                             ServerLevel level,
                             BlockPos center,
                             TripProfile profile,
                             int radius,
                             int baseY) {
        JOBS.put(sessionKey, new Job(level, center, profile, radius, baseY));

        // Ensure center exists immediately
        buildOneColumn(level, center.getX(), center.getZ(), center, profile, radius, baseY);
    }

    public static void tick() {
        if (JOBS.isEmpty()) return;

        for (var it = JOBS.entrySet().iterator(); it.hasNext(); ) {
            Job job = it.next().getValue();

            int done = 0;
            while (done < COLUMNS_PER_TICK && job.hasNext()) {
                int x = job.nextX();
                int z = job.nextZ();

                buildOneColumn(job.level, x, z, job.center, job.profile, job.radius, job.baseY);

                job.advance();
                done++;
            }

            if (!job.hasNext()) {
                TripDecorations.decorate(job.level, job.center, job.profile, job.radius, job.baseY);
                it.remove();
            }
        }
    }

    private static void buildOneColumn(ServerLevel level,
                                       int x, int z,
                                       BlockPos center,
                                       TripProfile profile,
                                       int radius,
                                       int baseY) {

        int dx = x - center.getX();
        int dz = z - center.getZ();

        float dist = Mth.sqrt(dx * dx + dz * dz);
        float d = Mth.clamp(dist / (float) radius, 0f, 1f);

        TripProfile.TerrainParams tp = profile.terrain;

        // main hills (0..1)
        float n = TripNoise.fbm(profile.seed, x, z, tp.baseFreq(), tp.octaves(), tp.lacunarity(), tp.persistence());
        float hills = (n - 0.5f) * tp.hillsAmp();

        // extra high-frequency detail
        float n2 = TripNoise.fbm(profile.seed ^ 0xC0FFEE, x, z,
                tp.baseFreq() * 3.2f, Math.max(2, tp.octaves() - 1),
                tp.lacunarity(), tp.persistence());
        float detail = (n2 - 0.5f) * tp.detailAmp();

        // valley lower near center
        float valley = (1f - d) * tp.valleyDepth() * tp.bowlStrength();

        // rim wall near border
        float t = Mth.clamp((d - tp.wallStart()) / (1f - tp.wallStart()), 0f, 1f);
        float wall = smoothstep(t) * tp.wallHeight();

        int height = baseY + Math.round(hills + detail - valley + wall);
        height = Mth.clamp(height, baseY - 18, baseY + 90);

        level.getChunk(x >> 4, z >> 4);

        TripProfile.Palette pal = profile.palette;

        int minY = baseY - 24;

        for (int y = minY; y <= height; y++) {
            int depth = height - y;

            BlockState state;
            if (depth == 0) state = pal.surface();
            else if (depth <= 4) state = pal.subsurface();
            else state = pal.stone();

            level.setBlock(new BlockPos(x, y, z), state, 2);
        }

        // sparse accent freckles on the surface
        if (depthHash(profile.seed, x, z) % 47 == 0) {
            level.setBlock(new BlockPos(x, height + 1, z), pal.accent(), 2);
        }
    }

    private static int depthHash(long seed, int x, int z) {
        long h = seed ^ (x * 73428767L) ^ (z * 912931L);
        h ^= (h >>> 33);
        h *= 0xff51afd7ed558ccdL;
        h ^= (h >>> 33);
        return (int) h;
    }

    private static float smoothstep(float t) {
        return t * t * (3f - 2f * t);
    }

    private static final class Job {
        final ServerLevel level;
        final BlockPos center;
        final TripProfile profile;
        final int radius;
        final int baseY;

        private int x;
        private int z;
        private final int minX, maxX, minZ, maxZ;

        Job(ServerLevel level, BlockPos center, TripProfile profile, int radius, int baseY) {
            this.level = level;
            this.center = center;
            this.profile = profile;
            this.radius = radius;
            this.baseY = baseY;

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