package com.zeus.cheat.module.impl.movement;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import com.zeus.cheat.value.ListValue;
import com.zeus.cheat.value.NumberValue;

@ModuleInfo(name = "Fly", category = Category.MOVEMENT, description = "Allows you to fly")
public class Fly extends Module {
    private final NumberValue speed = register(new NumberValue("Speed", 1.5, 0.1, 5.0));
    private final ListValue mode    = register(new ListValue("Mode", new String[]{"Vanilla", "Creative"}, "Vanilla"));

    @Override
    protected void onEnable() {
        if ("Creative".equals(mode.get())) mc.player.getAbilities().flying = true;
    }

    @Override
    protected void onDisable() {
        mc.player.getAbilities().flying = false;
        mc.player.setVelocity(0, 0, 0);
    }

    @EventListener
    public void onTick(EventTick e) {
        if (!"Vanilla".equals(mode.get())) return;
        double s = speed.get().doubleValue() * 0.2;
        mc.player.setVelocity(0, 0, 0);
        if (mc.options.jumpKey.isPressed())  mc.player.addVelocity(0,  s, 0);
        if (mc.options.sneakKey.isPressed()) mc.player.addVelocity(0, -s, 0);

        float yaw = (float) Math.toRadians(mc.player.getYaw());
        double fx = -Math.sin(yaw), fz = Math.cos(yaw);
        if (mc.options.forwardKey.isPressed()) mc.player.addVelocity(fx * s, 0, fz * s);
        if (mc.options.backKey.isPressed())    mc.player.addVelocity(-fx * s, 0, -fz * s);
        if (mc.options.leftKey.isPressed())    mc.player.addVelocity(fz * s, 0, -fx * s);
        if (mc.options.rightKey.isPressed())   mc.player.addVelocity(-fz * s, 0, fx * s);
    }
}
