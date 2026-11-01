package com.zeus.cheat.module.impl.render;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventRender3D;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import com.zeus.cheat.util.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@ModuleInfo(name = "ESP", category = Category.RENDER, description = "Highlights entities")
public class ESP extends Module {

    @EventListener
    public void onRender(EventRender3D e) {
        if (mc.world == null) return;
        for (Entity ent : mc.world.getEntities()) {
            if (!(ent instanceof LivingEntity) || ent == mc.player) continue;
            if (ent.isInvisible()) continue;
            int color = (ent instanceof PlayerEntity) ? 0xFFFF5555 : 0xFFFFFF55;
            RenderUtils.drawEntityBox(e.getMatrices(), ent, color, e.getTickDelta());
        }
    }
}
