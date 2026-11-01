package com.zeus.cheat;

import com.zeus.cheat.config.ConfigManager;
import com.zeus.cheat.event.EventBus;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.gui.clickgui.ClickGuiScreen;
import com.zeus.cheat.gui.hud.HudManager;
import com.zeus.cheat.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZeusClient implements ClientModInitializer {
    public static final String NAME = "Zeus";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final MinecraftClient mc = MinecraftClient.getInstance();

    public static EventBus eventBus;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;
    public static HudManager hudManager;

    public static KeyBinding clickGuiKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[{}] Loading v{}", NAME, VERSION);

        eventBus       = new EventBus();
        moduleManager  = new ModuleManager();
        configManager  = new ConfigManager();
        hudManager     = new HudManager();

        moduleManager.init();
        configManager.load();

        clickGuiKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.zeus.clickgui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.zeus")
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (clickGuiKey.wasPressed()) {
                client.setScreen(new ClickGuiScreen());
            }
            if (client.player != null && client.world != null) {
                eventBus.post(new EventTick());
            }
        });

        // ИСПРАВЛЕНО: ctx вместо matrices
        HudRenderCallback.EVENT.register((ctx, tickDelta) -> {
            hudManager.render(ctx, tickDelta);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(configManager::save));

        LOGGER.info("[{}] Loaded. {} modules.", NAME, moduleManager.getModules().size());
    }
}
