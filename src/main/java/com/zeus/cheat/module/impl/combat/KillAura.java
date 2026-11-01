package com.zeus.cheat.module.impl.combat;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import com.zeus.cheat.util.RotationManager;
import com.zeus.cheat.value.BoolValue;
import com.zeus.cheat.value.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "KillAura", category = Category.COMBAT, description = "Умная атака с предсказанием движения")
public class KillAura extends Module {
    private final NumberValue range = register(new NumberValue("Range", 4.5, 3.0, 6.0));
    private final NumberValue cps = register(new NumberValue("CPS", 12.0, 1.0, 20.0));
    private final BoolValue players = register(new BoolValue("Players", true));
    private final BoolValue mobs = register(new BoolValue("Mobs", false));
    private final BoolValue rotate = register(new BoolValue("Rotate", true));

    private long lastAttack = 0;

    @EventListener
    public void onTick(EventTick e) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        
        long delay = (long) (1000.0 / cps.get().doubleValue());
        if (System.currentTimeMillis() - lastAttack < delay) return;

        LivingEntity target = findTarget();
        if (target == null) {
            RotationManager.stop();
            return;
        }

        // Предсказание позиции на следующий тик (умная наводка)
        Vec3d predictedPos = target.getPos().add(target.getVelocity()).add(0, target.getHeight() / 2.0, 0);
        Vec3d playerPos = mc.player.getEyePos();
        Vec3d diff = predictedPos.subtract(playerPos);

        float yaw = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90.0F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diff.y, Math.hypot(diff.x, diff.z)));

        if (rotate.get()) {
            RotationManager.rotate(yaw, pitch);
        } else {
            mc.player.setYaw(yaw);
            mc.player.setPitch(pitch);
        }

        // Атака
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(mc.player.getActiveHand());
        lastAttack = System.currentTimeMillis();
    }

    private LivingEntity findTarget() {
        LivingEntity best = null;
        double bestDist = range.get().doubleValue();
        for (Entity ent : mc.world.getEntities()) {
            if (!(ent instanceof LivingEntity) || ent == mc.player) continue;
            if (ent.isInvisible() || ent.isSpectator() || ent.isDead()) continue;
            if (ent instanceof PlayerEntity && !players.get()) continue;
            if (!(ent instanceof PlayerEntity) && !mobs.get()) continue;
            
            double dist = mc.player.distanceTo(ent);
            if (dist < bestDist) {
                bestDist = dist;
                best = (LivingEntity) ent;
            }
        }
        return best;
    }
}
