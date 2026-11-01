package com.zeus.cheat.module.impl.combat;

import com.zeus.cheat.ZeusClient;
import com.zeus.cheat.module.Module;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class Velocity extends Module {

    public Velocity() {
        super("Velocity");
    }

    public void onPacketReceive(Object packet) {
        if (packet instanceof EntityVelocityUpdateS2CPacket p) {
            if (ZeusClient.mc.player == null) return;
            
            if (p.getEntityId() != ZeusClient.mc.player.getId()) return;
            
            // Ваша логика отмены отбрасывания (Velocity)
        }
    }
}
