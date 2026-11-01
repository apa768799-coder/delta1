package com.zeus.cheat.module.impl.combat;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventTick;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import com.zeus.cheat.value.BoolValue;
import com.zeus.cheat.value.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "KillAura", category = Category.COMBAT, description = "Attacks nearby entities")
public class KillAura extends Module {
    private final NumberValue range = register(new NumberValue("Range", 4.5, 3.0, 6.0));
    private final NumberValue cps   = register(new NumberValue("CPS", 12.0, 1.0, 20.0));
    private final BoolValue players = register(new BoolValue("Players", true));
    private final BoolValue mobs    = register(new BoolValue("Mobs", false));

    private long lastAttack = 0;

    @EventListener
    public void onTick(EventTick e) {
        if (mc.player == null || mc.world == null) return;
        long delay = (long) (1000.0 / cps.get().doubleValue());
        if (System.currentTimeMillis() - lastAttack < delay) return;

        LivingEntity target = findTarget();
        if (target == null) return;

        faceEntity(target);
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(mc.player.getActiveHand());
        lastAttack = System.currentTimeMillis();
    }

    private LivingEntity findTarget() {
        LivingEntity best = null;
        double bestDist = range.get().doubleValue();
        for (Entity ent : mc.world.getEntities()) {
            if (!(ent instanceof LivingEntity) || ent == mc.player) continue;
            if (ent.isInvisible() || ent.isSpectator()) continue;
            if (ent instanceof PlayerEntity && !players.get()) continue;
            if (!(ent instanceof PlayerEntity) && !mobs.get()) continue;
            double d = mc.player.distanceTo(ent);
            if (d < bestDist) { bestDist = d; best = (LivingEntity) ent; }
        }
        return best;
    }

    private void faceEntity(Entity e) {
        Vec3d diff = e.getPos().add(0, e.getHeight() / 2.0, 0).subtract(mc.player.getEyePos());
        float yaw   = (float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90f;
        float pitch = (float) -Math.toDegrees(Math.atan2(diff.y, Math.hypot(diff.x, diff.z)));
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }
}
