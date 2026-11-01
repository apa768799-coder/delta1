package com.zeus.cheat.module;

import com.zeus.cheat.ZeusClient;
import com.zeus.cheat.value.Value;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    protected final List<Value<?>> values = new ArrayList<>();

    private String name;
    private String description;
    private Category category;
    private int key;
    private boolean enabled;

    public Module() {
        ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);
        if (info == null) throw new IllegalStateException("Missing @ModuleInfo on " + getClass().getName());
        this.name = info.name();
        this.category = info.category();
        this.key = info.key();
        this.description = info.description();
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) { onEnable(); ZeusClient.eventBus.subscribe(this); }
        else         { onDisable(); ZeusClient.eventBus.unsubscribe(this); }
    }

    public void setEnabled(boolean state) {
        if (enabled == state) return;
        toggle();
    }

    protected void onEnable() {}
    protected void onDisable() {}

    public <T extends Value<?>> T register(T v) { values.add(v); return v; }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public int getKey() { return key; }
    public void setKey(int k) { this.key = k; }
    public boolean isEnabled() { return enabled; }
    public List<Value<?>> getValues() { return values; }
}
