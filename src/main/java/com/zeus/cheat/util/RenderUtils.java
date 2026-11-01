package com.zeus.cheat.util;

import com.zeus.cheat.ZeusClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderSystem;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class RenderUtils {
    public static void drawEntityBox(MatrixStack matrices, Entity entity, int color, float tickDelta) {
        Vec3d pos = entity.getPos().add(
            (entity.getX() - entity.lastRenderX) * tickDelta,
            (entity.getY() - entity.lastRenderY) * tickDelta,
            (entity.getZ() - entity.lastRenderZ) * tickDelta
        ).subtract(ZeusClient.mc.getEntityRenderDispatcher().camera.getPos());

        Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ())
                        .offset(pos.add(entity.getX(), entity.getY(), entity.getZ()));

        drawBox(matrices, box, color);
    }

    public static void drawBox(MatrixStack matrices, Box box, int color) {
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8)  & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f model = entry.getPositionMatrix();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        buf.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buf.vertex(model, (float)box.minX, (float)box.minY, (float)box.minZ).color(r, g, b, a * 0.3f).next();
        buf.vertex(model, (float)box.maxX, (float)box.minY, (float)box.minZ).color(r, g, b, a * 0.3f).next();
        buf.vertex(model, (float)box.maxX, (float)box.minY, (float)box.maxZ).color(r, g, b, a * 0.3f).next();
        buf.vertex(model, (float)box.minX, (float)box.minY, (float)box.maxZ).color(r, g, b, a * 0.3f).next();

        buf.vertex(model, (float)box.minX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a * 0.3f).next();
        buf.vertex(model, (float)box.maxX, (float)box.maxY, (float)box.maxZ).color(r, g, b, a * 0.3f).next();
        buf.vertex(model, (float)box.maxX, (float)box.maxY, (float)box.minZ).color(r, g, b, a * 0.3f).next();
        buf.vertex(model, (float)box.minX, (float)box.maxY, (float)box.minZ).color(r, g, b, a * 0.3f).next();
        tess.draw();

        buf.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        line(buf, model, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ, r, g, b, a);
        line(buf, model, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ, r, g, b, a);
        line(buf, model, box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ, r, g, b, a);
        line(buf, model, box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ, r, g, b, a);
        line(buf, model, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ, r, g, b, a);
        line(buf, model, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        line(buf, model, box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ, r, g, b, a);
        line(buf, model, box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ, r, g, b, a);
        line(buf, model, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ, r, g, b, a);
        line(buf, model, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ, r, g, b, a);
        line(buf, model, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        line(buf, model, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ, r, g, b, a);
        tess.draw();

        RenderSystem.disableBlend();
    }

    private static void line(BufferBuilder buf, Matrix4f m,
                             double x1, double y1, double z1,
                             double x2, double y2, double z2,
                             float r, float g, float b, float a) {
        buf.vertex(m, (float)x1, (float)y1, (float)z1).color(r, g, b, a).next();
        buf.vertex(m, (float)x2, (float)y2, (float)z2).color(r, g, b, a).next();
    }
}
