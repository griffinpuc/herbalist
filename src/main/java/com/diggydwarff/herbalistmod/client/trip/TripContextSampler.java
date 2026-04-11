package com.diggydwarff.herbalistmod.client.trip;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class TripContextSampler {

    public TripContext sample(Minecraft mc) {
        TripContext ctx = new TripContext();
        Level level = mc.level;
        if (level == null || mc.player == null) return ctx;

        BlockPos pos = mc.player.blockPosition();

        // Indoors: can’t see sky at head height
        ctx.indoors = !level.canSeeSky(pos.above());

        // Night: rough
        long dayTime = level.getDayTime() % 24000L;
        ctx.night = (dayTime >= 13000L && dayTime <= 23000L);

        // Near water: cheap scan in a small cube
        ctx.nearWater = scanForWater(level, pos, 6);

        return ctx;
    }

    private boolean scanForWater(Level level, BlockPos center, int r) {
        BlockPos.MutableBlockPos mp = new BlockPos.MutableBlockPos();
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    mp.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    if (level.getFluidState(mp).isSource()) return true;
                }
            }
        }
        return false;
    }
}