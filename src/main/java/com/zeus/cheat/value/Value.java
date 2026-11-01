package com.zeus.cheat.value;

public abstract class Value<T> {
    public final String name;
    protected T value;
    public Value(String name, T defaultValue) { this.name = name; this.value = defaultValue; }
    public T get() { return value; }
    public void set(T v) { this.value = v; }
    public String getName() { return name; }
}
