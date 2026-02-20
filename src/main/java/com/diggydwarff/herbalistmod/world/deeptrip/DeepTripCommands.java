package com.diggydwarff.herbalistmod.world.deeptrip;

import com.diggydwarff.herbalistmod.HerbalistMod;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HerbalistMod.MODID)
public final class DeepTripCommands {

    @SubscribeEvent
    public static void onRegister(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("deeptrip")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.literal("enter").executes(ctx -> {
                            var p = ctx.getSource().getPlayerOrException();
                            if (DeepTripManager.isActive(p)) {
                                ctx.getSource().sendFailure(Component.literal("Already in deep trip."));
                                return 0;
                            }
                            DeepTripManager.enter(p);
                            ctx.getSource().sendSuccess(() -> Component.literal("Entered deep trip."), false);
                            return 1;
                        }))
                        .then(Commands.literal("exit").executes(ctx -> {
                            var p = ctx.getSource().getPlayerOrException();
                            if (!DeepTripManager.isActive(p)) {
                                ctx.getSource().sendFailure(Component.literal("Not in deep trip."));
                                return 0;
                            }
                            DeepTripManager.exit(p);
                            ctx.getSource().sendSuccess(() -> Component.literal("Exited deep trip."), false);
                            return 1;
                        }))
        );
    }

    private DeepTripCommands() {}
}