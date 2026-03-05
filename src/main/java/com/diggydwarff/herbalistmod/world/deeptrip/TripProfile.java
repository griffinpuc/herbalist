package com.diggydwarff.herbalistmod.world.deeptrip;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class TripProfile {

    public final long seed;
    public final Palette palette;
    public final TerrainParams terrain;
    public final SkyParams sky;
    public final FaunaParams fauna;
    public final FeatureParams features;
    public final VisualParams visuals;

    private TripProfile(long seed,
                        Palette palette,
                        TerrainParams terrain,
                        SkyParams sky,
                        FaunaParams fauna,
                        FeatureParams features,
                        VisualParams visuals) {
        this.seed = seed;
        this.palette = palette;
        this.terrain = terrain;
        this.sky = sky;
        this.fauna = fauna;
        this.features = features;
        this.visuals = visuals;
    }

    public static TripProfile fromSeed(long seed) {
        RandomSource r = RandomSource.create(seed ^ 0x9E3779B97F4A7C15L);

        Palette palette = Palette.generate(r);
        TerrainParams terrain = TerrainParams.generate(r);
        SkyParams sky = SkyParams.generate(r);
        FaunaParams fauna = FaunaParams.generate(r);
        FeatureParams features = FeatureParams.generate(r, palette);
        VisualParams visuals = VisualParams.generate(r);

        return new TripProfile(seed, palette, terrain, sky, fauna, features, visuals);
    }

    // ---------------------------
    // Palette
    // ---------------------------

    public record Palette(
            BlockState surface,
            BlockState subsurface,
            BlockState stone,
            BlockState accent,
            BlockState foliage,
            BlockState trunk
    ) {
        private static final BlockState[] SURFACE = new BlockState[] {
                Blocks.GRASS_BLOCK.defaultBlockState(),
                Blocks.SAND.defaultBlockState(),
                Blocks.RED_SAND.defaultBlockState(),
                Blocks.MOSS_BLOCK.defaultBlockState(),
                Blocks.MYCELIUM.defaultBlockState(),
                Blocks.SOUL_SAND.defaultBlockState(),
                Blocks.CRIMSON_NYLIUM.defaultBlockState(),
                Blocks.WARPED_NYLIUM.defaultBlockState(),
                Blocks.SNOW_BLOCK.defaultBlockState(),
                Blocks.WHITE_WOOL.defaultBlockState(),
                Blocks.BLACK_WOOL.defaultBlockState()
        };

        private static final BlockState[] SUBSURFACE = new BlockState[] {
                Blocks.DIRT.defaultBlockState(),
                Blocks.COARSE_DIRT.defaultBlockState(),
                Blocks.PODZOL.defaultBlockState(),
                Blocks.CLAY.defaultBlockState(),
                Blocks.NETHERRACK.defaultBlockState(),
                Blocks.DEEPSLATE.defaultBlockState(),
                Blocks.GRAVEL.defaultBlockState(),
                Blocks.END_STONE.defaultBlockState()
        };

        private static final BlockState[] STONE = new BlockState[] {
                Blocks.STONE.defaultBlockState(),
                Blocks.ANDESITE.defaultBlockState(),
                Blocks.DIORITE.defaultBlockState(),
                Blocks.GRANITE.defaultBlockState(),
                Blocks.DEEPSLATE.defaultBlockState(),
                Blocks.BLACKSTONE.defaultBlockState(),
                Blocks.BASALT.defaultBlockState(),
                Blocks.END_STONE.defaultBlockState(),
                Blocks.TUFF.defaultBlockState()
        };

        // keep accents mostly “visual” (no diamond/gold spam)
        private static final BlockState[] ACCENT = new BlockState[] {
                Blocks.AMETHYST_BLOCK.defaultBlockState(),
                Blocks.BUDDING_AMETHYST.defaultBlockState(),
                Blocks.GLOWSTONE.defaultBlockState(),
                Blocks.SEA_LANTERN.defaultBlockState(),
                Blocks.SHROOMLIGHT.defaultBlockState(),
                Blocks.OBSIDIAN.defaultBlockState(),
                Blocks.QUARTZ_BLOCK.defaultBlockState(),
                Blocks.MAGENTA_STAINED_GLASS.defaultBlockState(),
                Blocks.CYAN_STAINED_GLASS.defaultBlockState(),
                Blocks.PINK_CONCRETE.defaultBlockState(),
                Blocks.LIME_CONCRETE.defaultBlockState()
        };

        private static final BlockState[] FOLIAGE = new BlockState[] {
                Blocks.OAK_LEAVES.defaultBlockState(),
                Blocks.BIRCH_LEAVES.defaultBlockState(),
                Blocks.SPRUCE_LEAVES.defaultBlockState(),
                Blocks.JUNGLE_LEAVES.defaultBlockState(),
                Blocks.DARK_OAK_LEAVES.defaultBlockState(),
                Blocks.AZALEA_LEAVES.defaultBlockState(),
                Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(),
                Blocks.WARPED_WART_BLOCK.defaultBlockState()
        };

        private static final BlockState[] TRUNK = new BlockState[] {
                Blocks.OAK_LOG.defaultBlockState(),
                Blocks.BIRCH_LOG.defaultBlockState(),
                Blocks.SPRUCE_LOG.defaultBlockState(),
                Blocks.JUNGLE_LOG.defaultBlockState(),
                Blocks.DARK_OAK_LOG.defaultBlockState(),
                Blocks.ACACIA_LOG.defaultBlockState(),
                Blocks.CRIMSON_STEM.defaultBlockState(),
                Blocks.WARPED_STEM.defaultBlockState()
        };

        static Palette generate(RandomSource r) {
            BlockState surface = pick(r, SURFACE);
            BlockState subsurface = pick(r, SUBSURFACE);
            BlockState stone = pick(r, STONE);
            BlockState accent = pick(r, ACCENT);
            BlockState foliage = pick(r, FOLIAGE);
            BlockState trunk = pick(r, TRUNK);

            String surfName = surface.getBlock().toString();
            if (surfName.contains("nylium") || surfName.contains("soul")) {
                subsurface = Blocks.NETHERRACK.defaultBlockState();
                if (r.nextFloat() < 0.6f) stone = Blocks.BLACKSTONE.defaultBlockState();
                if (r.nextFloat() < 0.45f) trunk = Blocks.CRIMSON_STEM.defaultBlockState();
            }

            return new Palette(surface, subsurface, stone, accent, foliage, trunk);
        }

        private static BlockState pick(RandomSource r, BlockState[] arr) {
            return arr[r.nextInt(arr.length)];
        }
    }

    // ---------------------------
    // Terrain
    // ---------------------------

    public record TerrainParams(
            float baseFreq,
            int octaves,
            float lacunarity,
            float persistence,
            float hillsAmp,
            float detailAmp,
            float valleyDepth,
            float wallStart,
            float wallHeight,
            float bowlStrength
    ) {
        static TerrainParams generate(RandomSource r) {
            float baseFreq = r.nextFloat() * 0.004f + 0.003f;    // 0.003..0.007
            int octaves = r.nextInt(3, 6);                       // 3..5
            float lacunarity = r.nextFloat() * 0.9f + 1.7f;      // 1.7..2.6
            float persistence = r.nextFloat() * 0.25f + 0.45f;   // 0.45..0.70

            float hillsAmp = r.nextFloat() * 28f + 18f;          // 18..46
            float detailAmp = r.nextFloat() * 10f + 4f;          // 4..14

            float valleyDepth = r.nextFloat() * 14f + 6f;        // 6..20
            float wallStart = r.nextFloat() * 0.18f + 0.68f;     // 0.68..0.86
            float wallHeight = r.nextFloat() * 38f + 20f;        // 20..58

            float bowlStrength = r.nextFloat() * 0.8f + 0.4f;    // 0.4..1.2

            return new TerrainParams(baseFreq, octaves, lacunarity, persistence,
                    hillsAmp, detailAmp, valleyDepth, wallStart, wallHeight, bowlStrength);
        }
    }

    // ---------------------------
    // Sky (placeholder data; your shader will dominate visuals anyway)
    // ---------------------------

    public record SkyParams(int skyRGB, int fogRGB) {
        static SkyParams generate(RandomSource r) {
            int sky = hsvToRgb(r.nextFloat(), 0.35f + r.nextFloat() * 0.55f, 0.55f + r.nextFloat() * 0.45f);
            int fog = hsvToRgb(r.nextFloat(), 0.45f + r.nextFloat() * 0.45f, 0.22f + r.nextFloat() * 0.35f);
            return new SkyParams(sky, fog);
        }

        private static int hsvToRgb(float h, float s, float v) {
            float rr, gg, bb;

            int i = (int) Math.floor(h * 6f);
            float f = h * 6f - i;
            float p = v * (1f - s);
            float q = v * (1f - f * s);
            float t = v * (1f - (1f - f) * s);

            switch (i % 6) {
                case 0 -> { rr = v; gg = t; bb = p; }
                case 1 -> { rr = q; gg = v; bb = p; }
                case 2 -> { rr = p; gg = v; bb = t; }
                case 3 -> { rr = p; gg = q; bb = v; }
                case 4 -> { rr = t; gg = p; bb = v; }
                default -> { rr = v; gg = p; bb = q; }
            }

            int ri = (int)(rr * 255f);
            int gi = (int)(gg * 255f);
            int bi = (int)(bb * 255f);
            return (ri << 16) | (gi << 8) | bi;
        }
    }

    // ---------------------------
    // Fauna
    // ---------------------------

    public record FaunaParams(DyeColor sheepColor, float density) {
        static FaunaParams generate(RandomSource r) {
            DyeColor[] colors = DyeColor.values();
            DyeColor c = colors[r.nextInt(colors.length)];
            float density = r.nextFloat() * 0.8f + 0.2f; // 0.2..1.0
            return new FaunaParams(c, density);
        }
    }

    // ---------------------------
    // Procedural Features (styles + weights)
    // ---------------------------

    public record FeatureParams(
            TreeStyle[] treeStyles,
            CrystalStyle[] crystalStyles,
            RuinStyle[] ruinStyles,
            PatchStyle[] patchStyles,
            float crystalWeight,
            float treeWeight,
            float ruinWeight,
            int scatterAttemptsPerRadius
    ) {
        static FeatureParams generate(RandomSource r, Palette pal) {
            float crystalW = 0.15f + r.nextFloat() * 0.25f; // 0.15..0.40
            float treeW = 0.20f + r.nextFloat() * 0.35f;    // 0.20..0.55
            float ruinW = 0.05f + r.nextFloat() * 0.18f;    // 0.05..0.23

            float sum = crystalW + treeW + ruinW;
            if (sum > 0.90f) {
                float k = 0.90f / sum;
                crystalW *= k; treeW *= k; ruinW *= k;
            }

            int treeStylesN = r.nextInt(2, 6);
            int crystalStylesN = r.nextInt(1, 4);
            int ruinStylesN = r.nextInt(1, 3);
            int patchStylesN = r.nextInt(2, 5);

            TreeStyle[] trees = new TreeStyle[treeStylesN];
            for (int i = 0; i < treeStylesN; i++) trees[i] = TreeStyle.generate(r, pal);

            CrystalStyle[] crystals = new CrystalStyle[crystalStylesN];
            for (int i = 0; i < crystalStylesN; i++) crystals[i] = CrystalStyle.generate(r, pal);

            RuinStyle[] ruins = new RuinStyle[ruinStylesN];
            for (int i = 0; i < ruinStylesN; i++) ruins[i] = RuinStyle.generate(r, pal);

            PatchStyle[] patches = new PatchStyle[patchStylesN];
            for (int i = 0; i < patchStylesN; i++) patches[i] = PatchStyle.generate(r, pal);

            int attemptsPerRadius = r.nextInt(3, 8);

            return new FeatureParams(trees, crystals, ruins, patches, crystalW, treeW, ruinW, attemptsPerRadius);
        }
    }

    public record TreeStyle(
            BlockState trunk,
            BlockState foliage,
            int minHeight,
            int maxHeight,
            int canopyRadius,
            float bendiness,
            float branchChance,
            float canopyHoles
    ) {
        static TreeStyle generate(RandomSource r, Palette pal) {
            int minH = r.nextInt(4, 7);
            int maxH = minH + r.nextInt(2, 7);
            int rad = r.nextInt(2, 5);

            float bend = r.nextFloat() * 4.0f;
            float branch = r.nextFloat() * 0.35f;
            float holes = r.nextFloat() * 0.25f;

            BlockState trunk = (r.nextFloat() < 0.78f) ? pal.trunk() : pal.stone();
            BlockState leaves = (r.nextFloat() < 0.82f) ? pal.foliage() : pal.accent();

            return new TreeStyle(trunk, leaves, minH, maxH, rad, bend, branch, holes);
        }
    }

    public record CrystalStyle(
            BlockState block,
            int minSpikes,
            int maxSpikes,
            int minHeight,
            int maxHeight,
            int spread,
            float branchChance
    ) {
        static CrystalStyle generate(RandomSource r, Palette pal) {
            BlockState b = (r.nextFloat() < 0.65f) ? pal.accent() : pal.stone();

            int minS = r.nextInt(2, 5);
            int maxS = minS + r.nextInt(1, 4);        // reduced spike growth

            int minH = r.nextInt(3, 6);
            int maxH = minH + r.nextInt(3, 8);        // reduced height growth

            int spread = r.nextInt(3, 8);
            float branch = r.nextFloat() * 0.22f;

            return new CrystalStyle(b, minS, maxS, minH, maxH, spread, branch);
        }
    }

    public record RuinStyle(
            BlockState block,
            int minPillars,
            int maxPillars,
            int minHeight,
            int maxHeight,
            int minSpacing,
            int maxSpacing,
            float archChance,
            float decayChance
    ) {
        static RuinStyle generate(RandomSource r, Palette pal) {
            BlockState b = (r.nextFloat() < 0.7f) ? pal.stone() : pal.subsurface();

            int minP = r.nextInt(3, 6);
            int maxP = minP + r.nextInt(2, 8);

            int minH = r.nextInt(3, 6);
            int maxH = minH + r.nextInt(3, 10);

            int minS = r.nextInt(3, 6);
            int maxS = minS + r.nextInt(2, 6);

            float arch = r.nextFloat() * 0.35f;
            float decay = 0.05f + r.nextFloat() * 0.35f;

            return new RuinStyle(b, minP, maxP, minH, maxH, minS, maxS, arch, decay);
        }
    }

    public record PatchStyle(
            BlockState block,
            int minCount,
            int maxCount,
            int radius,
            float placeChance
    ) {
        static PatchStyle generate(RandomSource r, Palette pal) {
            BlockState[] candidates = new BlockState[] {
                    Blocks.RED_MUSHROOM.defaultBlockState(),
                    Blocks.BROWN_MUSHROOM.defaultBlockState(),
                    Blocks.TORCHFLOWER.defaultBlockState(),
                    Blocks.ALLIUM.defaultBlockState(),
                    Blocks.AZALEA.defaultBlockState(),
                    Blocks.DEAD_BUSH.defaultBlockState(),
                    pal.accent(),
                    pal.subsurface()
            };

            BlockState b = candidates[r.nextInt(candidates.length)];
            int min = r.nextInt(6, 14);
            int max = min + r.nextInt(8, 28);
            int rad = r.nextInt(3, 9);
            float chance = 0.45f + r.nextFloat() * 0.45f;

            return new PatchStyle(b, min, max, rad, chance);
        }
    }

    // ---------------------------
    // Visuals (shader-driven)
    // ---------------------------

    public record VisualParams(
            float hueShift,
            float hueDriftSpeed,
            float saturation,
            float contrast,
            float exposure,
            float vignette
    ) {
        static VisualParams generate(RandomSource r) {
            float hueShift = r.nextFloat();
            float hueDrift = (r.nextFloat() * 2f - 1f) * 0.06f;

            float sat = 0.70f + r.nextFloat() * 1.60f;   // 0.70..2.30
            float con = 0.85f + r.nextFloat() * 0.75f;   // 0.85..1.60
            float exp = 0.88f + r.nextFloat() * 0.42f;   // 0.88..1.30

            float vig = (r.nextFloat() < 0.35f) ? (0.25f + r.nextFloat() * 0.45f) : (r.nextFloat() * 0.18f);

            return new VisualParams(hueShift, hueDrift, sat, con, exp, vig);
        }
    }
}