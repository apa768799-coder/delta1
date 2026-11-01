package com.zeus.cheat.module.impl.visuals;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventRender3D;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

@ModuleInfo(name = "Nametags", category = Category.VISUALS, description = "Custom entity nametags with health")
public class Nametags extends Module {
    @EventListener
    public void onRender(EventRender3D e) {
        if (mc.world == null) return;
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        
        for (Entity ent : mc.world.getEntities()) {
            if (ent == mc.player || !ent.isAlive()) continue;
            
            String name = ent.getName().getString();
            if (ent instanceof PlayerEntity p) {
                float health = p.getHealth() + p.getAbsorptionAmount();
                name += String.format(" §c%.1f", health);
            }

            MatrixStack matrices = e.getMatrices();
            matrices.push();
            double x = ent.prevX + (ent.getX() - ent.prevX) * e.getTickDelta();
            double y = ent.prevY + (ent.getY() - ent.prevY) * e.getTickDelta() + ent.getHeight() + 0.5;
            double z = ent.prevZ + (ent.getZ() - ent.prevZ) * e.getTickDelta();
            
            matrices.translate(x - mc.getEntityRenderDispatcher().camera.getPos().x, 
                               y - mc.getEntityRenderDispatcher().camera.getPos().y, 
                               z - mc.getEntityRenderDispatcher().camera.getPos().z);
            matrices.multiply(mc.getEntityRenderDispatcher().getRotation());
            matrices.scale(-0.025f, -0.025f, 0.025f);

            int width = mc.textRenderer.getWidth(name);
            mc.textRenderer.draw(Text.of(name), -width / 2f, 0, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), immediate, net.minecraft.client.font.TextRenderer.TextLayerType.SEE_THROUGH, 0, 15728880);
            immediate.draw();
            matrices.pop();
        }
    }
}
