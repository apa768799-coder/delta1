package com.zeus.cheat.gui.clickgui;

import com.zeus.cheat.ZeusClient;
import com.zeus.cheat.value.*;
import net.minecraft.client.gui.DrawContext;

public abstract class SettingComponent {
    public double x, y, width, height;
    public abstract void render(DrawContext ctx, int mx, int my);
    public boolean mouseClicked(double mx, double my, int btn) { return false; }
    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) { return false; }
    public boolean mouseScrolled(double mx, double my, double amt) { return false; }

    public static class BoolComp extends SettingComponent {
        final BoolValue value;
        public BoolComp(BoolValue v, double x, double y, double w, double h) {
            this.value = v; this.x = x; this.y = y; this.width = w; this.height = h;
        }
        public void render(DrawContext ctx, int mx, int my) {
            ctx.fill((int)x, (int)y, (int)(x+width), (int)(y+height), 0xFF1A1A1A);
            int bx = (int)(x + width - 12), by = (int)y + 2;
            ctx.fill(bx, by, bx+10, by+10, value.get() ? 0xFF55FFFF : 0xFF444444);
            ctx.drawText(ZeusClient.mc.textRenderer, value.getName(), (int)x + 4, (int)y + 3, 0xFFCCCCCC, true);
        }
        public boolean mouseClicked(double mx, double my, int btn) {
            if (mx >= x && mx <= x + width && my >= y && my <= y + height) {
                value.set(!value.get()); return true;
            }
            return false;
        }
    }

    public static class NumberComp extends SettingComponent {
        final NumberValue value; boolean dragging;
        public NumberComp(NumberValue v, double x, double y, double w, double h) {
            this.value = v; this.x = x; this.y = y; this.width = w; this.height = h;
        }
        public void render(DrawContext ctx, int mx, int my) {
            ctx.fill((int)x, (int)y, (int)(x+width), (int)(y+height), 0xFF1A1A1A);
            double pct = (value.get().doubleValue() - value.min.doubleValue())
                       / (value.max.doubleValue() - value.min.doubleValue());
            int sx = (int)(x + 2), sy = (int)(y + height - 3);
            ctx.fill(sx, sy, (int)(x+width-2), sy+1, 0xFF333333);
            ctx.fill(sx, sy, (int)(sx + (width-4)*pct), sy+1, 0xFF55FFFF);
            ctx.drawText(ZeusClient.mc.textRenderer,
                value.getName() + ": " + value.get(), (int)x + 4, (int)y + 2, 0xFFCCCCCC, true);
        }
        public boolean mouseClicked(double mx, double my, int btn) {
            if (mx >= x && mx <= x + width && my >= y && my <= y + height) {
                dragging = true; update(mx); return true;
            }
            return false;
        }
        public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
            if (dragging) { update(mx); return true; }
            return false;
        }
        public boolean mouseScrolled(double mx, double my, double amt) {
            if (!(mx >= x && mx <= x + width && my >= y && my <= y + height)) return false;
            double step = (value.max.doubleValue() - value.min.doubleValue()) / 50.0;
            value.set(value.get().doubleValue() + amt * step);
            return true;
        }
        private void update(double mx) {
            double pct = (mx - x - 2) / (width - 4);
            pct = Math.max(0, Math.min(1, pct));
            value.set(value.min.doubleValue() + pct * (value.max.doubleValue() - value.min.doubleValue()));
        }
    }

    public static class ListComp extends SettingComponent {
        final ListValue value;
        public ListComp(ListValue v, double x, double y, double w, double h) {
            this.value = v; this.x = x; this.y = y; this.width = w; this.height = h;
        }
        public void render(DrawContext ctx, int mx, int my) {
            ctx.fill((int)x, (int)y, (int)(x+width), (int)(y+height), 0xFF1A1A1A);
            ctx.drawText(ZeusClient.mc.textRenderer,
                value.getName() + ": " + value.get(), (int)x + 4, (int)y + 3, 0xFFCCCCCC, true);
        }
        public boolean mouseClicked(double mx, double my, int btn) {
            if (mx >= x && mx <= x + width && my >= y && my <= y + height) {
                value.increment(); return true;
            }
            return false;
        }
    }
}
