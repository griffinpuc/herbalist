package com.diggydwarff.herbalistmod.world.deeptrip;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public record TripTheme(
        String id,
        BlockState surface,
        BlockState subsurface,
        BlockState stone,
        BlockState accent,
        int skyColorRGB,
        int fogColorRGB,
        List<ThemeMobs.MobEntry> mobs,
        float weirdness
) {

    public static TripTheme fromSeed(long seed, String forcedThemeId) {

        String themeId = forcedThemeId;

        if (themeId == null || themeId.isBlank()) {
            int idx = Math.floorMod((int)seed, 3);
            themeId = switch (idx) {
                case 0 -> "forest_candy";
                case 1 -> "nether_gilded";
                default -> "deep_dark_redsky";
            };
        }

        return switch (themeId) {

            case "nether_gilded" -> new TripTheme(
                    "nether_gilded",
                    Blocks.GOLD_BLOCK.defaultBlockState(),
                    Blocks.NETHERRACK.defaultBlockState(),
                    Blocks.BLACKSTONE.defaultBlockState(),
                    Blocks.GLOWSTONE.defaultBlockState(),
                    0x2b001f,
                    0x3a0030,
                    ThemeMobs.netherGilded(seed),
                    0.85f
            );

            case "deep_dark_redsky" -> new TripTheme(
                    "deep_dark_redsky",
                    Blocks.BLACK_WOOL.defaultBlockState(),
                    Blocks.DEEPSLATE.defaultBlockState(),
                    Blocks.DEEPSLATE.defaultBlockState(),
                    Blocks.SCULK.defaultBlockState(),
                    0x4a0000,
                    0x120000,
                    ThemeMobs.deepDark(seed),
                    0.6f
            );

            default -> new TripTheme(
                    "forest_candy",
                    Blocks.GRASS_BLOCK.defaultBlockState(),
                    Blocks.DIRT.defaultBlockState(),
                    Blocks.STONE.defaultBlockState(),
                    Blocks.PINK_CONCRETE.defaultBlockState(),
                    0xffb7ff,
                    0xffc7e8,
                    ThemeMobs.forestCandy(seed),
                    0.75f
            );
        };
    }
}