package com.zeus.cheat.module.impl.visuals;

import com.zeus.cheat.event.EventListener;
import com.zeus.cheat.event.impl.EventRender3D;
import com.zeus.cheat.module.Category;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.module.ModuleInfo;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderSystem;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

@ModuleInfo(name = "Tracers", category = Category.VISUALS, description = "Draws lines to entities")
public class Tracers extends Module {
    @EventListener
    public void onRender(EventRender3D e) {
        if (mc.world == null) return;
        
        MatrixStack matrices = e.getMatrices();
        matrices.push();
        matrices.translate(
            -mc.getEntityRenderDispatcher().camera.getPos().x, 
            -mc.getEntityRenderDispatcher().camera.getPos().y, 
            -mc.getEntityRenderDispatcher().camera.getPos().z
        );

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();
        
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        buf.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        for (Entity ent : mc.world.getEntities()) {
            if (ent == mc.player) continue;
            
            int color = (ent instanceof PlayerEntity) ? 0xFFFF5555 : 0xFFFFFF55;
            float a = ((color >> 24) & 0xFF) / 255f;
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;

            Vec3d pos = ent.getPos().add(0, ent.getHeight() / 2.0, 0);
            Matrix4f model = matrices.peek().getPositionMatrix();
            
            buf.vertex(model, (float)mc.player.getX(), (float)(mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose())), (float)mc.player.getZ()).color(r, g, b, a).next();
            buf.vertex(model, (float)pos.x, (float)pos.y, (float)pos.z).color(r, g, b, a).next();
        }
        
        tess.draw();
        RenderSystem.disableBlend();
        matrices.pop();
    }
}
