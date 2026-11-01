package com.zeus.cheat.gui.clickgui;

import com.zeus.cheat.module.Category;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private final List<Panel> panels = new ArrayList<>();

    public ClickGuiScreen() {
        super(Text.literal("Zeus ClickGUI"));
        int x = 20;
        for (Category c : Category.values()) {
            panels.add(new Panel(c, x, 20, 110, 18));
            x += 120;
        }
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0x88000000);
        for (Panel p : panels) p.render(ctx, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        for (Panel p : panels) if (p.mouseClicked(x, y, button)) return true;
        return super.mouseClicked(x, y, button);
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        for (Panel p : panels) p.mouseReleased(x, y, button);
        return super.mouseReleased(x, y, button);
    }

    @Override
    public boolean mouseDragged(double x, double y, int button, double dx, double dy) {
        for (Panel p : panels) if (p.mouseDragged(x, y, button, dx, dy)) return true;
        return super.mouseDragged(x, y, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double hAmount, double vAmount) {
        for (Panel p : panels) if (p.mouseScrolled(x, y, vAmount)) return true;
        return super.mouseScrolled(x, y, hAmount, vAmount);
    }

    @Override
    public boolean shouldPause() { return false; }
}
