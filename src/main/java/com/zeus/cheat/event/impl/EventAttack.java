package com.zeus.cheat.event.impl;

import com.zeus.cheat.event.CancellableEvent;
import net.minecraft.entity.Entity;

public class EventAttack extends CancellableEvent {
    private final Entity target;
    public EventAttack(Entity t) { this.target = t; }
    public Entity getTarget() { return target; }
}
