package com.zeus.cheat.event.impl;

import net.minecraft.client.gui.DrawContext;

public class EventRender2D {
    private final DrawContext context;
    private final float tickDelta;
    public EventRender2D(DrawContext ctx, float delta) { this.context = ctx; this.tickDelta = delta; }
    public DrawContext getContext() { return context; }
    public float getTickDelta() { return tickDelta; }
}
