package com.zeus.cheat.gui.clickgui;

import com.zeus.cheat.ZeusClient;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.value.*;
import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton {
    public final Module module;
    public double x, y, width, height = 16;
    public boolean extended;
    private final List<SettingComponent> components = new ArrayList<>();

    public ModuleButton(Module m, double x, double y, double w, double h) {
        this.module = m; this.x = x; this.y = y; this.width = w; this.height = h;
        double cy = y + h;
        for (Value<?> v : m.getValues()) {
            SettingComponent c;
            if (v instanceof BoolValue)        c = new SettingComponent.BoolComp((BoolValue) v, x, cy, w, 14);
            else if (v instanceof NumberValue) c = new SettingComponent.NumberComp((NumberValue) v, x, cy, w, 14);
            else if (v instanceof ListValue)   c = new SettingComponent.ListComp((ListValue) v, x, cy, w, 14);
            else continue;
            components.add(c);
            cy += 14;
        }
    }

    public void render(DrawContext ctx, int mx, int my) {
        boolean hov = isHovered(mx, my);
        int bg = module.isEnabled() ? 0xFF2A4A6A : (hov ? 0xFF333333 : 0xFF1A1A1A);
        ctx.fill((int)x, (int)y, (int)(x+width), (int)(y+height), bg);
        int text = module.isEnabled() ? 0xFF55FFFF : 0xFFCCCCCC;
        ctx.drawText(ZeusClient.mc.textRenderer, module.getName(), (int)x + 4, (int)y + 4, text, true);

        if (!extended) return;
        double cy = y + height;
        for (SettingComponent c : components) {
            c.x = x; c.y = cy;
            c.render(ctx, mx, my);
            cy += c.height;
        }
    }

    public double getHeight() {
        if (!extended) return height;
        double h = height;
        for (SettingComponent c : components) h += c.height;
        return h;
    }

    public boolean mouseClicked(double mx, double my, int btn) {
        if (isHovered(mx, my)) {
            if (btn == 0) module.toggle();
            else if (btn == 1 && !components.isEmpty()) extended = !extended;
            return true;
        }
        if (extended) for (SettingComponent c : components) if (c.mouseClicked(mx, my, btn)) return true;
        return false;
    }

    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (extended) for (SettingComponent c : components) if (c.mouseDragged(mx, my, btn, dx, dy)) return true;
        return false;
    }

    public boolean mouseScrolled(double mx, double my, double amt) {
        if (extended) for (SettingComponent c : components) if (c.mouseScrolled(mx, my, amt)) return true;
        return false;
    }

    private boolean isHovered(double mx, double my) {
        return mx >= x && mx <= x + width && my >= y && my <= y + height;
    }
}
