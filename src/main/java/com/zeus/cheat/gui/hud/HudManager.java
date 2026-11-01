package com.zeus.cheat.gui.hud;

import com.zeus.cheat.ZeusClient;
import com.zeus.cheat.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HudManager {
    public void render(MatrixStack matrices, float delta) {
        DrawContext ctx = new DrawContext(ZeusClient.mc, ZeusClient.mc.getBufferBuilders().getEntityVertexConsumers());
        renderArrayList(ctx);
        renderWatermark(ctx);
    }

    private void renderArrayList(DrawContext ctx) {
        List<Module> active = ZeusClient.moduleManager.getModules().stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparingInt(m -> -ZeusClient.mc.textRenderer.getWidth(m.getName())))
                .collect(Collectors.toList());

        int y = 4;
        for (int i = 0; i < active.size(); i++) {
            Module m = active.get(i);
            String text = m.getName();
            int w = ZeusClient.mc.textRenderer.getWidth(text);
            int x = ZeusClient.mc.getWindow().getScaledWidth() - w - 4;

            float hue = (System.currentTimeMillis() % 5000) / 5000f + i * 0.03f;
            int color = hsvToRgb(hue % 1f, 0.7f, 1f);

            ctx.fill(x - 2, y - 1, ZeusClient.mc.getWindow().getScaledWidth(), y + 10, 0x88000000);
            ctx.drawText(ZeusClient.mc.textRenderer, text, x, y, color, true);
            y += 11;
        }
    }

    private void renderWatermark(DrawContext ctx) {
        String text = ZeusClient.NAME + " v" + ZeusClient.VERSION;
        ctx.fill(2, 2, ZeusClient.mc.textRenderer.getWidth(text) + 8, 14, 0xAA000000);
        ctx.drawText(ZeusClient.mc.textRenderer, text, 4, 4, 0xFF55FFFF, true);
    }

    private int hsvToRgb(float h, float s, float v) {
        int i = (int)(h * 6); float f = h * 6 - i;
        float p = v * (1 - s), q = v * (1 - f * s), t = v * (1 - (1 - f) * s);
        float r, g, b;
        switch (i % 6) {
            case 0: r=v; g=t; b=p; break; case 1: r=q; g=v; b=p; break;
            case 2: r=p; g=v; b=t; break; case 3: r=p; g=q; b=v; break;
            case 4: r=t; g=p; b=v; break; default: r=v; g=p; b=q; break;
        }
        return (255 << 24) | ((int)(r*255) << 16) | ((int)(g*255) << 8) | (int)(b*255);
    }
}
