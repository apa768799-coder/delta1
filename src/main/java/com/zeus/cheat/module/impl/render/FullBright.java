package com.zeus.cheat.module.impl.render;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;

@ModuleInfo(name = "FullBright", category = Category.RENDER, description = "Max gamma")
public class FullBright extends Module {
    private double oldGamma;

    @Override
    protected void onEnable() {
        oldGamma = mc.options.getGamma().getValue();
        mc.options.getGamma().setValue(16.0);
    }

    @Override
    protected void onDisable() {
        mc.options.getGamma().setValue(oldGamma);
    }

    @EventListener
    public void onTick(EventTick e) {
        if (mc.options.getGamma().getValue() < 16.0) mc.options.getGamma().setValue(16.0);
    }
}
