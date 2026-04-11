package com.diggydwarff.herbalistmod.world.deeptrip;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class TripDecorations {
    private TripDecorations() {}

    public static void decorate(ServerLevel level, BlockPos center, TripProfile profile, int radius, int baseY) {
        RandomSource r = RandomSource.create(profile.seed ^ 0xD00DFEEDL);

        TripProfile.FeatureParams fp = profile.features;
        int clusters = Math.max(8, radius / 26);                 // overall variety sites
        int itemsPerClusterMin = 12;
        int itemsPerClusterMax = 34;

        for (int c = 0; c < clusters; c++) {

            // pick a cluster center inside the circle
            int cx, cz;
            for (int tries = 0; ; tries++) {
                cx = center.getX() + r.nextInt(-radius, radius + 1);
                cz = center.getZ() + r.nextInt(-radius, radius + 1);
                int dx = cx - center.getX();
                int dz = cz - center.getZ();
                if (dx * dx + dz * dz <= radius * radius) break;
                if (tries > 50) break;
            }

            float cd = dist01(cx, cz, center, radius);

            // band: 0..0.35 calm, 0.35..0.75 mixed, 0.75..1 wild rim
            BandWeights bw = bandWeights(fp, cd, r);

            // cluster type bias: each cluster leans one way
            ClusterLean lean = pickClusterLean(r);

            int items = r.nextInt(itemsPerClusterMin, itemsPerClusterMax + 1);
            int spread = r.nextInt(8, 18); // gaussian-ish scatter radius

            for (int i = 0; i < items; i++) {
                int x = cx + (int) Math.round(r.nextGaussian() * spread);
                int z = cz + (int) Math.round(r.nextGaussian() * spread);

                int dx = x - center.getX();
                int dz = z - center.getZ();
                if (dx * dx + dz * dz > radius * radius) continue;

                float d = dist01(x, z, center, radius);

                // density: calmer near center, wilder outward
                float densityGate = 0.25f + (d * d) * 0.85f;
                if (r.nextFloat() > densityGate) continue;

                int y = findTop(level, x, z, baseY + 180);
                if (y <= -60) continue;

                BlockPos pos = new BlockPos(x, y, z);

                // life filler: little stones / ground cover sometimes
                if (r.nextFloat() < 0.22f) {
                    sprinkleGroundCover(level, pos, r, profile);
                }

                // pick feature using band weights + cluster lean
                float roll = r.nextFloat();
                roll = applyLean(roll, lean);

                if (roll < bw.crystal) {
                    placeCrystal(level, pos, r, profile);
                } else if (roll < bw.crystal + bw.tree) {
                    placeTree(level, pos, r, profile);
                } else if (roll < bw.crystal + bw.tree + bw.ruin) {
                    placeRuin(level, pos, r, profile);
                } else {
                    placePatch(level, pos, r, profile);
                }

                // occasional fallen log (adds realism fast)
                if (r.nextFloat() < 0.06f) {
                    placeFallenLog(level, pos, r, profile);
                }
            }
        }
    }

    // -------------------------
    // Bands / Weights
    // -------------------------

    private record BandWeights(float crystal, float tree, float ruin) {}

    private static BandWeights bandWeights(TripProfile.FeatureParams fp, float d, RandomSource r) {
        // start from profile weights
        float c = fp.crystalWeight();
        float t = fp.treeWeight();
        float u = fp.ruinWeight();

        // band shaping
        if (d < 0.35f) {
            // calm valley floor
            c *= 0.35f;
            u *= 0.55f;
            t *= 1.35f;
        } else if (d > 0.75f) {
            // rim wild
            c *= 1.55f;
            u *= 1.35f;
            t *= 0.70f;
        } else {
            // mid mixed
            c *= 1.05f;
            u *= 1.05f;
            t *= 1.00f;
        }

        // ensure sum <= 0.90 (rest is patches)
        float sum = c + t + u;
        if (sum > 0.90f) {
            float k = 0.90f / sum;
            c *= k; t *= k; u *= k;
        }

        return new BandWeights(c, t, u);
    }

    private enum ClusterLean { TREES, CRYSTALS, RUINS, MIXED }

    private static ClusterLean pickClusterLean(RandomSource r) {
        float v = r.nextFloat();
        if (v < 0.30f) return ClusterLean.TREES;
        if (v < 0.55f) return ClusterLean.CRYSTALS;
        if (v < 0.75f) return ClusterLean.RUINS;
        return ClusterLean.MIXED;
    }

    private static float applyLean(float roll, ClusterLean lean) {
        // roll remapping to bias a region without hard-forcing it
        return switch (lean) {
            case TREES -> roll * 0.92f;
            case CRYSTALS -> 0.04f + roll * 0.96f;
            case RUINS -> 0.08f + roll * 0.92f;
            case MIXED -> roll;
        };
    }

    private static float dist01(int x, int z, BlockPos center, int radius) {
        int dx = x - center.getX();
        int dz = z - center.getZ();
        float dist = Mth.sqrt(dx * dx + dz * dz);
        return Mth.clamp(dist / (float) radius, 0f, 1f);
    }

    // -------------------------
    // Trees (procedural style)
    // -------------------------
    private static void placeTree(ServerLevel level, BlockPos pos, RandomSource r, TripProfile profile) {
        TripProfile.TreeStyle s = profile.features.treeStyles()[r.nextInt(profile.features.treeStyles().length)];

        int h = r.nextInt(s.minHeight(), s.maxHeight() + 1);
        float bend = s.bendiness();

        int x0 = pos.getX();
        int z0 = pos.getZ();
        int y0 = pos.getY();

        // don't plant on air / avoid stacking on decorations
        if (level.getBlockState(pos.below()).isAir()) return;

        // trunk with bending
        float fx = x0 + 0.5f;
        float fz = z0 + 0.5f;
        float dirX = (r.nextFloat() * 2f - 1f);
        float dirZ = (r.nextFloat() * 2f - 1f);
        float dirLen = Mth.sqrt(dirX * dirX + dirZ * dirZ);
        if (dirLen < 0.001f) { dirX = 1; dirZ = 0; } else { dirX /= dirLen; dirZ /= dirLen; }

        int lastTx = x0, lastTz = z0;

        for (int i = 0; i < h; i++) {
            float t = i / (float) Math.max(1, h - 1);
            float off = bend * (t * t);
            int tx = Mth.floor(fx + dirX * off);
            int tz = Mth.floor(fz + dirZ * off);

            lastTx = tx; lastTz = tz;

            BlockPos p = new BlockPos(tx, y0 + i, tz);
            level.setBlock(p, s.trunk(), 2);

            // occasional branches
            if (i > 2 && r.nextFloat() < s.branchChance()) {
                int bx = tx + r.nextInt(-1, 2);
                int bz = tz + r.nextInt(-1, 2);
                level.setBlock(new BlockPos(bx, y0 + i, bz), s.trunk(), 2);
            }
        }

        // canopy around actual top
        BlockPos top = new BlockPos(lastTx, y0 + h, lastTz);
        int rad = s.canopyRadius();

        for (int dx = -rad; dx <= rad; dx++) {
            for (int dz = -rad; dz <= rad; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int d2 = dx * dx + dz * dz + dy * dy * 2;
                    if (d2 > rad * rad + 1) continue;
                    if (r.nextFloat() < s.canopyHoles()) continue;

                    // mostly foliage, sometimes accent (fantasy highlight)
                    BlockState leaf = (r.nextFloat() < 0.88f) ? s.foliage() : profile.palette.accent();
                    level.setBlock(top.offset(dx, dy, dz), leaf, 2);
                }
            }
        }
    }

    // -------------------------
    // Crystals (tapered shards, not pillars)
    // -------------------------
    private static void placeCrystal(ServerLevel level, BlockPos pos, RandomSource r, TripProfile profile) {
        TripProfile.CrystalStyle s = profile.features.crystalStyles()[r.nextInt(profile.features.crystalStyles().length)];

        // fewer spikes overall; feels natural
        int spikes = Mth.clamp(r.nextInt(s.minSpikes(), s.maxSpikes() + 1), 2, 8);
        int baseY = pos.getY();

        for (int i = 0; i < spikes; i++) {
            int x = pos.getX() + r.nextInt(-s.spread(), s.spread() + 1);
            int z = pos.getZ() + r.nextInt(-s.spread(), s.spread() + 1);

            int h = r.nextInt(s.minHeight(), s.maxHeight() + 1);
            h = Math.min(h, 14); // hard cap to prevent skyscraper pillars

            // Build a shard with taper + occasional side shards near top
            for (int j = 0; j < h; j++) {
                float t = j / (float) h;
                float taperChance = 0.92f - t * 0.55f; // high at base -> low at tip
                if (r.nextFloat() > taperChance) continue;

                level.setBlock(new BlockPos(x, baseY + j, z), s.block(), 2);

                if (j > h / 2 && r.nextFloat() < s.branchChance() * 0.6f) {
                    int bx = x + r.nextInt(-1, 2);
                    int bz = z + r.nextInt(-1, 2);
                    level.setBlock(new BlockPos(bx, baseY + j, bz), s.block(), 2);
                }
            }

            // tiny tip highlight sometimes
            if (r.nextFloat() < 0.18f) {
                level.setBlock(new BlockPos(x, baseY + h, z), profile.palette.accent(), 2);
            }
        }
    }

    // -------------------------
    // Ruins (procedural style)
    // -------------------------
    private static void placeRuin(ServerLevel level, BlockPos pos, RandomSource r, TripProfile profile) {
        TripProfile.RuinStyle s = profile.features.ruinStyles()[r.nextInt(profile.features.ruinStyles().length)];

        int pillars = r.nextInt(s.minPillars(), s.maxPillars() + 1);
        int spacing = r.nextInt(s.minSpacing(), s.maxSpacing() + 1);

        for (int i = 0; i < pillars; i++) {
            int x = pos.getX() + r.nextInt(-spacing, spacing + 1) + r.nextInt(-spacing, spacing + 1);
            int z = pos.getZ() + r.nextInt(-spacing, spacing + 1) + r.nextInt(-spacing, spacing + 1);
            int y = pos.getY();

            int h = r.nextInt(s.minHeight(), s.maxHeight() + 1);
            h = Math.min(h, 12);

            for (int j = 0; j < h; j++) {
                if (r.nextFloat() < s.decayChance()) continue;
                level.setBlock(new BlockPos(x, y + j, z), s.block(), 2);
            }

            if (r.nextFloat() < s.archChance()) {
                int ax = x + r.nextInt(-2, 3);
                int az = z + r.nextInt(-2, 3);
                buildArch(level, new BlockPos(ax, y + h / 2, az), s.block(), r, s.decayChance());
            }

            // rare accent “glyph”
            if (r.nextFloat() < 0.08f) {
                level.setBlock(new BlockPos(x, y + 1, z), profile.palette.accent(), 2);
            }
        }
    }

    private static void buildArch(ServerLevel level, BlockPos pos, BlockState block, RandomSource r, float decay) {
        int w = r.nextInt(3, 7);
        int h = r.nextInt(3, 6);

        for (int x = -w; x <= w; x++) {
            int y = (int) Math.round(h * (1.0 - (x * x) / (double) (w * w)));
            if (r.nextFloat() < decay) continue;
            level.setBlock(pos.offset(x, y, 0), block, 2);
        }
    }

    // -------------------------
    // Patches
    // -------------------------
    private static void placePatch(ServerLevel level, BlockPos pos, RandomSource r, TripProfile profile) {
        TripProfile.PatchStyle s = profile.features.patchStyles()[r.nextInt(profile.features.patchStyles().length)];

        int n = r.nextInt(s.minCount(), s.maxCount() + 1);
        for (int i = 0; i < n; i++) {
            int x = pos.getX() + r.nextInt(-s.radius(), s.radius() + 1);
            int z = pos.getZ() + r.nextInt(-s.radius(), s.radius() + 1);
            int y = pos.getY();

            if (!level.getBlockState(new BlockPos(x, y - 1, z)).isAir()) {
                if (r.nextFloat() < s.placeChance()) {
                    level.setBlock(new BlockPos(x, y, z), s.block(), 2);
                }
            }
        }
    }

    // -------------------------
    // “Alive” fillers
    // -------------------------
    private static void sprinkleGroundCover(ServerLevel level, BlockPos pos, RandomSource r, TripProfile profile) {
        // small stones + grass-ish stuff; low cost, big realism boost
        BlockState[] cover = new BlockState[] {
                Blocks.MOSS_CARPET.defaultBlockState(),
                Blocks.GRASS.defaultBlockState(),
                Blocks.FERN.defaultBlockState(),
                Blocks.DEAD_BUSH.defaultBlockState(),
                Blocks.COBBLESTONE.defaultBlockState(),
                profile.palette.subsurface()
        };

        int n = r.nextInt(3, 10);
        for (int i = 0; i < n; i++) {
            int x = pos.getX() + r.nextInt(-3, 4);
            int z = pos.getZ() + r.nextInt(-3, 4);
            int y = pos.getY();

            if (level.getBlockState(new BlockPos(x, y, z)).isAir()
                    && !level.getBlockState(new BlockPos(x, y - 1, z)).isAir()) {
                level.setBlock(new BlockPos(x, y, z), cover[r.nextInt(cover.length)], 2);
            }
        }
    }

    private static void placeFallenLog(ServerLevel level, BlockPos pos, RandomSource r, TripProfile profile) {
        BlockState log = profile.palette.trunk();

        int len = r.nextInt(3, 8);
        boolean alongX = r.nextBoolean();

        int x0 = pos.getX();
        int y0 = pos.getY();
        int z0 = pos.getZ();

        for (int i = 0; i < len; i++) {
            int x = x0 + (alongX ? i : 0);
            int z = z0 + (alongX ? 0 : i);

            BlockPos p = new BlockPos(x, y0, z);
            if (!level.getBlockState(p).isAir()) continue;
            if (level.getBlockState(p.below()).isAir()) continue;
            level.setBlock(p, log, 2);
        }
    }

    // -------------------------
    // Helpers
    // -------------------------
    public static int findTop(ServerLevel level, int x, int z, int startY) {
        for (int y = startY; y > -64; y--) {
            if (!level.getBlockState(new BlockPos(x, y, z)).isAir()) return y + 1;
        }
        return -999;
    }
}