package com.zeus.cheat.gui.clickgui;

import com.zeus.cheat.ZeusClient;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.value.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton {
    public final Module module;
    public double x, y, width, height = 18;
    public boolean extended;
    private final List<SettingComponent> components = new ArrayList<>();
    private boolean binding = false;

    public ModuleButton(Module m, double x, double y, double w, double h) {
        this.module = m; this.x = x; this.y = y; this.width = w; this.height = h;
        double cy = y + h;
        for (Value<?> v : m.getValues()) {
            SettingComponent c;
            if (v instanceof BoolValue) c = new SettingComponent.BoolComp((BoolValue) v, x, cy, w, 16);
            else if (v instanceof NumberValue) c = new SettingComponent.NumberComp((NumberValue) v, x, cy, w, 16);
            else if (v instanceof ListValue) c = new SettingComponent.ListComp((ListValue) v, x, cy, w, 16);
            else continue;
            components.add(c);
            cy += 16;
        }
    }

    public void render(DrawContext ctx, int mx, int my) {
        boolean hovered = isHovered(mx, my);
        
        int bgColor;
        if (module.isEnabled()) {
            bgColor = 0xFF2A4A6A; 
        } else if (hovered) {
            bgColor = 0xFF333333; 
        } else {
            bgColor = 0xFF1A1A1A; 
        }

        ctx.fill((int)x, (int)y, (int)(x+width), (int)(y+height), bgColor);
        
        String text = module.getName();
        if (module.getKey() != 0 && !binding) {
            text += " §7[" + InputUtil.fromKeyCode(module.getKey(), 0).getLocalizedText().getString() + "]";
        } else if (binding) {
            text += " §e[Press Key...]";
        }
        
        int textColor = module.isEnabled() ? 0xFFFFFFFF : 0xFFCCCCCC;
        ctx.drawText(ZeusClient.mc.textRenderer, text, (int)x + 5, (int)y + 5, textColor, true);

        if (!extended) return;
        double cy = y + height;
        for (SettingComponent c : components) {
            c.x = x; c.y = cy;
            c.render(ctx, mx, my);
            cy += c.height;
        }
        ctx.fill((int)x, (int)(y+height), (int)(x+width), (int)cy, 0xCC111111);
    }

    public double getHeight() {
        if (!extended) return height;
        double h = height;
        for (SettingComponent c : components) h += c.height;
        return h;
    }

    public boolean mouseClicked(double mx, double my, int btn) {
        if (isHovered(mx, my)) {
            if (btn == 0) {
                if (binding) binding = false;
                else module.toggle();
                return true;
            } else if (btn == 1) {
                binding = true;
                return true;
            } else if (btn == 2 && !components.isEmpty()) {
                extended = !extended;
                return true;
            }
        }
        if (extended) for (SettingComponent c : components) if (c.mouseClicked(mx, my, btn)) return true;
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (binding) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) module.setKey(0);
            else module.setKey(keyCode);
            binding = false;
            return true;
        }
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
