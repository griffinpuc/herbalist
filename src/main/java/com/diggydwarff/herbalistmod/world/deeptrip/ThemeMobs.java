package com.diggydwarff.herbalistmod.world.deeptrip;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;

import java.util.List;

public final class ThemeMobs {
    private ThemeMobs() {}

    public record MobEntry(
            EntityType<?> type,
            int minCount,
            int maxCount,
            DyeColor sheepColor
    ) {}

    public static List<MobEntry> netherGilded(long seed) {
        return List.of(
                new MobEntry(EntityType.SHEEP, 6, 14, DyeColor.LIME),
                new MobEntry(EntityType.PIGLIN, 1, 3, null)
        );
    }

    public static List<MobEntry> deepDark(long seed) {
        return List.of(
                new MobEntry(EntityType.WARDEN, 1, 2, null),
                new MobEntry(EntityType.BAT, 6, 12, null)
        );
    }

    public static List<MobEntry> forestCandy(long seed) {
        return List.of(
                new MobEntry(EntityType.RABBIT, 6, 14, null),
                new MobEntry(EntityType.SHEEP, 3, 8, DyeColor.PINK)
        );
    }
}