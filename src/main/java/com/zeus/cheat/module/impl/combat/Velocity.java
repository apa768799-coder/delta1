package com.zeus.cheat.module.impl.combat;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventPacket;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import com.zeus.cheat.value.NumberValue;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

@ModuleInfo(name = "Velocity", category = Category.COMBAT, description = "Reduces knockback")
public class Velocity extends Module {
    private final NumberValue horizontal = register(new NumberValue("H", 0, 0, 100));
    private final NumberValue vertical = register(new NumberValue("V", 0, 0, 100));

    @EventListener
    public void onPacket(EventPacket e) {
        if (!(e.getPacket() instanceof EntityVelocityUpdateS2CPacket p)) return;
        if (p.getId() != mc.player.getId()) return; 
        
        double h = horizontal.get().doubleValue() / 100.0;
        double v = vertical.get().doubleValue() / 100.0;
        e.cancel();
        mc.player.setVelocity(mc.player.getVelocity().x * h, p.getVelocityY() / 8000.0 * v, mc.player.getVelocity().z * h);
    }
}
