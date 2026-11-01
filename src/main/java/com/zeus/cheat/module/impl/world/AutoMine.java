package com.zeus.cheat.module.impl.world;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventPacket;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import com.zeus.cheat.util.RotationManager;
import com.zeus.cheat.util.ToolUtils;
import com.zeus.cheat.value.BoolValue;
import com.zeus.cheat.value.NumberValue;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

@ModuleInfo(name = "AutoMine", category = Category.WORLD, description = "Автоматическая добыча блока в прицеле с умным подбором инструмента")
public class AutoMine extends Module {
    private final NumberValue range = register(new NumberValue("Range", 5.0, 3.0, 6.0));
    private final BoolValue autoTool = register(new BoolValue("AutoTool", true));
    private final BoolValue instantBreak = register(new BoolValue("InstantBreak", true));
    private final BoolValue rotate = register(new BoolValue("Rotate", true));

    private BlockPos currentTarget = null;
    private int lastToolSlot = -1;

    @EventListener
    public void onTick(EventTick e) {
        if (mc.player == null || mc.world == null) return;

        BlockPos target = getTargetBlock();

        // Если цель пропала или мы смотрим в воздух — сбрасываем состояние
        if (target == null) {
            resetState();
            return;
        }

        // 1. Плавная ротация к центру блока
        if (rotate.get()) {
            Vec3d center = new Vec3d(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
            Vec3d diff = center.subtract(mc.player.getEyePos());
            float yaw = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0F;
            float pitch = (float) -Math.toDegrees(Math.atan2(diff.y, Math.hypot(diff.x, diff.z)));
            RotationManager.rotate(yaw, pitch);
        }

        // 2. Silent Auto-Tool
        BlockState state = mc.world.getBlockState(target);
        if (autoTool.get()) {
            int bestSlot = ToolUtils.findBestTool(state);
            if (bestSlot != -1 && bestSlot != mc.player.getInventory().selectedSlot) {
                if (lastToolSlot == -1) lastToolSlot = mc.player.getInventory().selectedSlot;
                
                mc.player.getInventory().selectedSlot = bestSlot;
                mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(bestSlot));
            }
        }

        // 3. Логика ломания
        if (!target.equals(currentTarget)) {
            // Новая цель — начинаем ломать
            mc.interactionManager.updateBlockBreakingProgress(target, mc.player.horizontalFacing);
            currentTarget = target;
        } else {
            // Цель та же — форсим мгновенное разрушение
            if (instantBreak.get()) {
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                    PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                    currentTarget, mc.player.horizontalFacing
                ));
            }
        }
    }

    @EventListener(priority = 100)
    public void onPacket(EventPacket e) {
        // Перехватываем пакет остановки, чтобы предотвратить сброс прогресса на сервере
        if (e.getPacket() instanceof PlayerActionC2SPacket p && instantBreak.get()) {
            if (p.getAction() == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
                // Если блок еще не сломан, отменяем пакет и отправляем START заново
                if (mc.world.getBlockState(currentTarget).getHardness(mc.world, currentTarget) > 0) {
                    e.cancel();
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                        currentTarget, mc.player.horizontalFacing
                    ));
                }
            }
        }
    }

    @Override
    protected void onDisable() {
        resetState();
        // Корректно сообщаем серверу, что мы перестали ломать блок (чтобы не было десинка)
        if (mc.player != null && currentTarget != null) {
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK,
                currentTarget, mc.player.horizontalFacing
            ));
        }
    }

    private void resetState() {
        // Возвращаем исходный слот хотбара
        if (lastToolSlot != -1) {
            mc.player.getInventory().selectedSlot = lastToolSlot;
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(lastToolSlot));
            lastToolSlot = -1;
        }
        currentTarget = null;
    }

    private BlockPos getTargetBlock() {
        // Кастомный Raycast с учетом нашего Range
        BlockHitResult result = mc.world.raycast(new RaycastContext(
            mc.player.getEyePos(),
            mc.player.getEyePos().add(mc.player.getRotationVec(1.0f).multiply(range.get().doubleValue())),
            RaycastContext.ShapeType.OUTLINE,
            RaycastContext.FluidHandling.NONE,
            mc.player
        ));
        
        if (result != null && result.getType() == BlockHitResult.Type.BLOCK) {
            return result.getBlockPos();
        }
        return null;
    }
}
