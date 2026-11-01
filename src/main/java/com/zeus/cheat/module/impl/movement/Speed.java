package com.zeus.cheat.module.impl.movement;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import com.zeus.cheat.value.NumberValue;

@ModuleInfo(name = "Speed", category = Category.MOVEMENT, description = "Move faster")
public class Speed extends Module {
    private final NumberValue multiplier = register(new NumberValue("Multiplier", 1.5, 1.0, 5.0));

    @EventListener
    public void onTick(EventTick e) {
        if (mc.player == null) return;
        if (mc.player.input.movementForward == 0 && mc.player.input.movementSideways == 0) return;
        double m = multiplier.get().doubleValue();
        mc.player.setVelocity(
            mc.player.getVelocity().x * m,
            mc.player.getVelocity().y,
            mc.player.getVelocity().z * m
        );
    }
}
