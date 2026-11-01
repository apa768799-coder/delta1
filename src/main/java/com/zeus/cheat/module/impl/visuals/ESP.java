package com.zeus.cheat.module.impl.visuals;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventRender3D;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import com.zeus.cheat.util.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@ModuleInfo(name = "ESP", category = Category.VISUALS, description = "Подсветка сущностей с учетом здоровья")
public class ESP extends Module {
    @EventListener
    public void onRender(EventRender3D e) {
        if (mc.world == null) return;
        for (Entity ent : mc.world.getEntities()) {
            if (!(ent instanceof LivingEntity) || ent == mc.player) continue;
            if (ent.isInvisible() || ent.isSpectator()) continue;

            // Цвет зависит от типа: Игрок = красный, Моб = желтый, Дружелюбный = зеленый
            int color;
            if (ent instanceof PlayerEntity) {
                PlayerEntity p = (PlayerEntity) ent;
                float health = p.getHealth() + p.getAbsorptionAmount();
                // Интерполяция цвета от красного (0 HP) к зеленому (20 HP)
                int r = (int) (255 * (1 - health / 20f));
                int g = (int) (255 * (health / 20f));
                color = (255 << 24) | (r << 16) | (g << 8) | 255;
            } else {
                color = 0xFFFFFF55; // Желтый для мобов
            }

            RenderUtils.drawEntityBox(e.getMatrices(), ent, color, e.getTickDelta());
        }
    }
}
