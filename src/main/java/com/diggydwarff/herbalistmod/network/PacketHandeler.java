package com.diggydwarff.herbalistmod.network;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class PacketHandeler {

    private PacketHandeler() {}

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HerbalistMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    public static void register() {
        CHANNEL.messageBuilder(TripTransitionS2CPacket.class, id++)
                .encoder(TripTransitionS2CPacket::encode)
                .decoder(TripTransitionS2CPacket::decode)
                .consumerMainThread(TripTransitionS2CPacket::handle)
                .add();
    }
}