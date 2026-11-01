package com.zeus.cheat;

import com.zeus.cheat.manager.HudManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class ZeusClient implements ClientModInitializer {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    private HudManager hudManager;

    @Override
    public void onInitializeClient() {
        hudManager = new HudManager();
        
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> {
            float tickDelta = tickCounter.getTickDelta(true);
            hudManager.render(ctx, tickDelta);
        });
    }
}
