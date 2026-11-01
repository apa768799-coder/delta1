package com.zeus.cheat.event;

public class CancellableEvent {
    private boolean cancelled;
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean c) { this.cancelled = c; }
    public void cancel() { setCancelled(true); }
}
