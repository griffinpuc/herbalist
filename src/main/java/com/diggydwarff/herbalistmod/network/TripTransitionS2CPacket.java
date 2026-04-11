package com.diggydwarff.herbalistmod.network;

import com.diggydwarff.herbalistmod.client.handler.TripTransitionClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record TripTransitionS2CPacket(
        boolean entering,
        int fadeOutTicks,
        int holdTicks,
        int fadeInTicks,
        boolean lockControls,
        long deepTripSeed,     // 0 if not used
        long deepTripSessionId // 0 if not used
) {
    public static void encode(TripTransitionS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.entering);
        buf.writeVarInt(msg.fadeOutTicks);
        buf.writeVarInt(msg.holdTicks);
        buf.writeVarInt(msg.fadeInTicks);
        buf.writeBoolean(msg.lockControls);
        buf.writeLong(msg.deepTripSeed);
        buf.writeLong(msg.deepTripSessionId);
    }

    public static TripTransitionS2CPacket decode(FriendlyByteBuf buf) {
        return new TripTransitionS2CPacket(
                buf.readBoolean(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readBoolean(),
                buf.readLong(),
                buf.readLong()
        );
    }

    public static void handle(TripTransitionS2CPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> TripTransitionClient.get().start(msg));
        ctx.get().setPacketHandled(true);
    }
}