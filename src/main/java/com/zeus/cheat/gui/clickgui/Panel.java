package com.zeus.cheat.gui.clickgui;

import com.zeus.cheat.ZeusClient;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public class Panel {
    public final Category category;
    public double x, y, width, height;
    private final List<ModuleButton> buttons = new ArrayList<>();
    public boolean extended = true;
    private boolean dragging;
    private double dragX, dragY;

    public Panel(Category c, double x, double y, double w, double h) {
        this.category = c; this.x = x; this.y = y; this.width = w; this.height = h;
        double by = y + h;
        for (Module m : ZeusClient.moduleManager.getModulesByCategory(c)) {
            buttons.add(new ModuleButton(m, x, by, w, 16));
            by += 16;
        }
    }

    public void render(DrawContext ctx, int mx, int my) {
        int headerColor = isHovered(mx, my) ? 0xFF3A3A3A : 0xFF222222;
        ctx.fill((int)x, (int)y, (int)(x+width), (int)(y+height), headerColor);
        ctx.drawText(ZeusClient.mc.textRenderer, category.displayName, (int)x + 4, (int)y + 4, 0xFF55FFFF, true);

        if (!extended) return;
        double by = y + height;
        for (ModuleButton b : buttons) {
            b.x = x; b.y = by;
            b.render(ctx, mx, my);
            by += b.getHeight();
        }
        ctx.fill((int)x, (int)(y+height), (int)(x+width), (int)by, 0xCC111111);
    }

    public double getHeight() {
        if (!extended) return height;
        double h = height;
        for (ModuleButton b : buttons) h += b.getHeight();
        return h;
    }

    public boolean mouseClicked(double mx, double my, int btn) {
        if (isHovered(mx, my) && btn == 0) { dragging = true; dragX = mx - x; dragY = my - y; return true; }
        if (isHovered(mx, my) && btn == 1) { extended = !extended; return true; }
        if (extended) for (ModuleButton b : buttons) if (b.mouseClicked(mx, my, btn)) return true;
        return false;
    }

    public void mouseReleased(double mx, double my, int btn) { dragging = false; }

    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (dragging) { x = mx - dragX; y = my - dragY; return true; }
        if (extended) for (ModuleButton b : buttons) if (b.mouseDragged(mx, my, btn, dx, dy)) return true;
        return false;
    }

    public boolean mouseScrolled(double mx, double my, double amt) {
        if (extended) for (ModuleButton b : buttons) if (b.mouseScrolled(mx, my, amt)) return true;
        return false;
    }

    private boolean isHovered(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
}
