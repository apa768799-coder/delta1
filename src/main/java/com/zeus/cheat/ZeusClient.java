package com.zeus.cheat;

// ВНИМАНИЕ: Проверьте путь к вашему HudManager. 
// Если он в папке ui, замените manager на ui.
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
            // В 1.21.4 извлекаем float из RenderTickCounter
            float tickDelta = tickCounter.getTickDelta(true);
            hudManager.render(ctx, tickDelta);
        });
    }
}
