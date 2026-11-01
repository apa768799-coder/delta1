package com.zeus.cheat.util;

import com.zeus.cheat.ZeusClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

public class RotationManager {
    private static float serverYaw;
    private static float serverPitch;
    private static float targetYaw;
    private static float targetPitch;
    private static boolean rotating = false;

    public static void onUpdate() {
        if (rotating) {
            float deltaYaw = MathHelper.wrapDegrees(targetYaw - ZeusClient.mc.player.getYaw());
            float deltaPitch = MathHelper.wrapDegrees(targetPitch - ZeusClient.mc.player.getPitch());
            
            ZeusClient.mc.player.setYaw(ZeusClient.mc.player.getYaw() + deltaYaw * 0.4f); // Плавный поворот
            ZeusClient.mc.player.setPitch(ZeusClient.mc.player.getPitch() + deltaPitch * 0.4f);
            
            // Отправляем пакет, чтобы сервер синхронизировал состояние
            ZeusClient.mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                ZeusClient.mc.player.getYaw(), ZeusClient.mc.mc.player.getPitch(), ZeusClient.mc.player.isOnGround()
            ));
            
            if (Math.abs(deltaYaw) < 1.0f && Math.abs(deltaPitch) < 1.0f) {
                rotating = false;
            }
        } else {
            serverYaw = ZeusClient.mc.player.getYaw();
            serverPitch = ZeusClient.mc.player.getPitch();
        }
    }

    public static void rotate(float yaw, float pitch) {
        targetYaw = yaw;
        targetPitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
        rotating = true;
    }

    public static void stop() {
        rotating = false;
    }
}
