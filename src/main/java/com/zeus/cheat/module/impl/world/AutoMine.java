package com.zeus.cheat.module.impl.world;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import com.zeus.cheat.util.RotationManager;
import com.zeus.cheat.value.BoolValue;
import com.zeus.cheat.value.NumberValue;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

@ModuleInfo(name = "AutoMine", category = Category.WORLD, description = "Instant block breaking")
public class AutoMine extends Module {
    private final NumberValue range = register(new NumberValue("Range", 5.0, 3.0, 6.0));
    private final BoolValue rotate = register(new BoolValue("Rotate", true));

    private BlockPos currentTarget = null;

    @EventListener
    public void onTick(EventTick e) {
        if (mc.player == null || mc.world == null) return;

        BlockPos target = getTargetBlock();
        if (target == null) {
            if (currentTarget != null) abort(currentTarget);
            return;
        }

        if (rotate.get()) {
            Vec3d center = new Vec3d(target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5);
            Vec3d diff = center.subtract(mc.player.getEyePos());
            float yaw = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0F;
            float pitch = (float) -Math.toDegrees(Math.atan2(diff.y, Math.hypot(diff.x, diff.z)));
            RotationManager.rotate(yaw, pitch);
        }

        if (!target.equals(currentTarget)) {
            start(target);
            currentTarget = target;
        } else {
            // Instant break spam
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, target, Direction.UP));
            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, target, Direction.UP));
        }
    }

    @Override
    protected void onDisable() {
        if (currentTarget != null) abort(currentTarget);
        currentTarget = null;
    }

    private void start(BlockPos pos) {
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.UP));
    }

    private void abort(BlockPos pos) {
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, Direction.UP));
    }

    private BlockPos getTargetBlock() {
        BlockHitResult result = mc.world.raycast(new RaycastContext(
            mc.player.getEyePos(),
            mc.player.getEyePos().add(mc.player.getRotationVec(1.0f).multiply(range.get().doubleValue())),
            RaycastContext.ShapeType.OUTLINE,
            RaycastContext.FluidHandling.NONE,
            mc.player
        ));
        return (result != null && result.getType() == BlockHitResult.Type.BLOCK) ? result.getBlockPos() : null;
    }
}
