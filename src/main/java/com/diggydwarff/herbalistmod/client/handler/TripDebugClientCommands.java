package com.diggydwarff.herbalistmod.client.handler;

import com.diggydwarff.herbalistmod.HerbalistMod;
import com.diggydwarff.herbalistmod.client.trip.TripDirector;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID, value = Dist.CLIENT)
public final class TripDebugClientCommands {

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("tripdebug")
                        .then(Commands.literal("start")
                                .then(Commands.argument("seconds", IntegerArgumentType.integer(1, 60))
                                        .executes(ctx -> {
                                            int sec = IntegerArgumentType.getInteger(ctx, "seconds");
                                            TripDirector.get().debugStartCycle(sec);
                                            msg("Trip debug cycle started (" + sec + "s per primitive).");
                                            return 1;
                                        }))
                                .executes(ctx -> {
                                    TripDirector.get().debugStartCycle(5);
                                    msg("Trip debug cycle started (5s per primitive).");
                                    return 1;
                                })
                        )
                        .then(Commands.literal("stop").executes(ctx -> {
                            TripDirector.get().debugStopCycle();
                            msg("Trip debug stopped.");
                            return 1;
                        }))
                        .then(Commands.literal("next").executes(ctx -> {
                            TripDirector.get().debugNext();
                            msg("Trip debug: next primitive.");
                            return 1;
                        }))
                        .then(Commands.literal("set")
                                .then(Commands.argument("id", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            String id = StringArgumentType.getString(ctx, "id").trim();
                                            TripDirector.get().debugSet(id);
                                            msg("Trip debug forced primitive: " + id);
                                            return 1;
                                        }))
                        )
                        .then(Commands.literal("list").executes(ctx -> {
                            var ids = TripDirector.get().debugListIds();
                            msg("Trip primitives (" + ids.size() + "):");
                            for (String id : ids) msg(" - " + id);
                            return 1;
                        }))
        );
    }

    private static void msg(String s) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) mc.player.sendSystemMessage(Component.literal(s));
    }
}