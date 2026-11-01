package com.zeus.cheat.event.impl;

import net.minecraft.client.util.math.MatrixStack;

public class EventRender3D {
    private final MatrixStack matrices;
    private final float tickDelta;
    public EventRender3D(MatrixStack m, float delta) { this.matrices = m; this.tickDelta = delta; }
    public MatrixStack getMatrices() { return matrices; }
    public float getTickDelta() { return tickDelta; }
}
