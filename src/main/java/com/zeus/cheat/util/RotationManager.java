package com.zeus.cheat.util;

import com.zeus.cheat.ZeusClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class RotationManager {
    
    public static void sendRotation(float newYaw, float newPitch) {
        if (ZeusClient.mc.player == null || ZeusClient.mc.getNetworkHandler() == null) return;
        
        // ИСПРАВЛЕНИЕ: добавлен 4-й аргумент (false) для horizontalCollision
        ZeusClient.mc.getNetworkHandler().sendPacket(
            new PlayerMoveC2SPacket.LookAndOnGround(
                newYaw, 
                newPitch, 
                ZeusClient.mc.player.isOnGround(), 
                false
            )
        );
    }
}
