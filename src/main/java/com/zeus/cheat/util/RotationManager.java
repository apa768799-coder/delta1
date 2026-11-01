package com.zeus.cheat.util;

import com.zeus.cheat.ZeusClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

public class RotationManager {
    private static float targetYaw;
    private static float targetPitch;
    private static boolean isRotating = false;

    public static void rotate(float yaw, float pitch) {
        targetYaw = yaw;
        targetPitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        isRotating = true;
    }

    public static void stop() {
        isRotating = false;
    }

    public static void onUpdate() {
        if (!isRotating || ZeusClient.mc.player == null) return;
        
        float currentYaw = ZeusClient.mc.player.getYaw();
        float currentPitch = ZeusClient.mc.player.getPitch();
        
        float deltaYaw = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float deltaPitch = MathHelper.wrapDegrees(targetPitch - currentPitch);
        
        // Плавный поворот (можно настроить скорость)
        float smoothFactor = 0.2f; 
        float newYaw = currentYaw + deltaYaw * smoothFactor;
        float newPitch = currentPitch + deltaPitch * smoothFactor;

        ZeusClient.mc.player.setYaw(newYaw);
        ZeusClient.mc.player.setPitch(newPitch);
        
        ZeusClient.mc.getNetworkHandler().sendPacket(
            new PlayerMoveC2SPacket.LookAndOnGround(newYaw, newPitch, ZeusClient.mc.player.isOnGround())
        );
        
        if (Math.abs(deltaYaw) < 0.1f && Math.abs(deltaPitch) < 0.1f) {
            isRotating = false;
        }
    }
}
