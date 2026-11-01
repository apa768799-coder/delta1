package com.zeus.cheat.event.impl;

import com.zeus.cheat.event.CancellableEvent;
import net.minecraft.network.packet.Packet;

public class EventPacket extends CancellableEvent {
    private final Packet<?> packet;
    public EventPacket(Packet<?> p) { this.packet = p; }
    public Packet<?> getPacket() { return packet; }
}
