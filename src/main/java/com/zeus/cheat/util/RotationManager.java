package com.zeus.cheat.util;

import com.zeus.cheat.ZeusClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

public class RotationManager {
    private static float serverYaw;
    private static float serverPitch;
    private static float currentYaw;
    private static float currentPitch;
    private static boolean isRotating = false;

    public static void calculateAndApply(net.minecraft.entity.player.PlayerEntity player, net.minecraft.entity.LivingEntity target, boolean predict, int ticks, String mode, float smoothSpeed) {
        net.minecraft.util.math.Vec3d targetPos = target.getPos();
        
        if (predict) {
            targetPos = targetPos.add(target.getVelocity().multiply(ticks));
        }

        targetPos = targetPos.add(0, target.getEyeHeight(target.getPose()) / 2.0, 0);
        net.minecraft.util.math.Vec3d diff = targetPos.subtract(player.getEyePos());

        double dist = Math.sqrt(diff.x * diff.x + diff.z * diff.z);
        float targetPitchVal = (float) Math.toDegrees(Math.atan2(diff.y, dist));
        float targetYawVal = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0F;

        if (mode.equals("Snap") || mode.equals("Silent")) {
            if (mode.equals("Snap")) {
                player.setYaw(targetYawVal);
                player.setPitch(targetPitchVal);
            }
            sendLookPacket(targetYawVal, targetPitchVal, player.isOnGround());
        } else if (mode.equals("Smooth")) {
            float deltaYaw = MathHelper.wrapDegrees(targetYawVal - player.getYaw());
            float deltaPitch = MathHelper.wrapDegrees(targetPitchVal - player.getPitch());
            
            float smoothFactor = Math.min(smoothSpeed / 100.0f, 1.0f);
            currentYaw = player.getYaw() + deltaYaw * smoothFactor;
            currentPitch = player.getPitch() + deltaPitch * smoothFactor;

            player.setYaw(currentYaw);
            player.setPitch(currentPitch);
            sendLookPacket(currentYaw, currentPitch, player.isOnGround());
        }
    }

    private static void sendLookPacket(float yaw, float pitch, boolean onGround) {
        if (ZeusClient.mc.getNetworkHandler() != null) {
            ZeusClient.mc.getNetworkHandler().sendPacket(
                new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, onGround)
            );
        }
    }

    public static void reset() {
        isRotating = false;
    }
}
