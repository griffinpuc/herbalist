package com.diggydwarff.herbalistmod.client.handler;

import com.diggydwarff.herbalistmod.network.TripTransitionS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

public final class TripTransitionClient {

    private static final TripTransitionClient INSTANCE = new TripTransitionClient();
    public static TripTransitionClient get() { return INSTANCE; }

    private enum Phase { IDLE, FADE_OUT, HOLD, FADE_IN }

    private Phase phase = Phase.IDLE;
    private int t = 0;

    private int fadeOutTicks = 10;
    private int holdTicks = 12;
    private int fadeInTicks = 10;

    private boolean lockControls = true;
    private float alpha = 0f;

    // DeepTrip visuals context (set on enter, cleared on exit start)
    private long deepTripSeed = 0L;
    private long deepTripSessionId = 0L;

    private TripTransitionClient() {}

    public void start(TripTransitionS2CPacket msg) {
        this.fadeOutTicks = Math.max(1, msg.fadeOutTicks());
        this.holdTicks = Math.max(0, msg.holdTicks());
        this.fadeInTicks = Math.max(1, msg.fadeInTicks());
        this.lockControls = msg.lockControls();

        if (phase != Phase.IDLE) return;

        if (msg.entering()) {
            this.deepTripSeed = msg.deepTripSeed();
            this.deepTripSessionId = msg.deepTripSessionId();
        } else {
            // clear immediately on exit request (so shader shuts off as we leave)
            this.deepTripSeed = 0L;
            this.deepTripSessionId = 0L;
        }

        this.phase = Phase.FADE_OUT;
        this.t = 0;
        this.alpha = 0f;
    }

    public void tick(Minecraft mc) {
        if (phase == Phase.IDLE) return;

        int max = fadeOutTicks + holdTicks + fadeInTicks + 80;
        if (t > max) {
            phase = Phase.IDLE;
            t = 0;
            alpha = 0f;
            return;
        }

        if (lockControls) lockInputs(mc);

        t++;

        switch (phase) {
            case FADE_OUT -> {
                alpha = Mth.clamp(t / (float) fadeOutTicks, 0f, 1f);
                if (t >= fadeOutTicks) {
                    phase = Phase.HOLD;
                    t = 0;
                    alpha = 1f;
                }
            }
            case HOLD -> {
                alpha = 1f;
                if (t >= holdTicks) {
                    phase = Phase.FADE_IN;
                    t = 0;
                }
            }
            case FADE_IN -> {
                alpha = 1f - Mth.clamp(t / (float) fadeInTicks, 0f, 1f);
                if (t >= fadeInTicks) {
                    phase = Phase.IDLE;
                    t = 0;
                    alpha = 0f;
                }
            }
            default -> {}
        }

        if (phase != Phase.IDLE && t > max) {
            phase = Phase.IDLE;
            t = 0;
            alpha = 0f;
        }

    }

    public float alpha() { return alpha; }
    public boolean active() { return phase != Phase.IDLE; }

    public long deepTripSeed() { return deepTripSeed; }
    public long deepTripSessionId() { return deepTripSessionId; }

    private static void lockInputs(Minecraft mc) {
        LocalPlayer p = mc.player;
        if (p == null) return;

        mc.options.keyUp.setDown(false);
        mc.options.keyDown.setDown(false);
        mc.options.keyLeft.setDown(false);
        mc.options.keyRight.setDown(false);
        mc.options.keyJump.setDown(false);
        mc.options.keyShift.setDown(false);
        mc.options.keySprint.setDown(false);

        Input in = p.input;
        if (in != null) {
            in.leftImpulse = 0;
            in.forwardImpulse = 0;
            in.jumping = false;
            in.shiftKeyDown = false;
        }

        p.setDeltaMovement(0, p.getDeltaMovement().y, 0);
    }
}