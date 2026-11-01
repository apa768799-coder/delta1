package com.zeus.cheat.value;

public class ListValue extends Value<String> {
    public final String[] modes;
    public ListValue(String n, String[] modes, String def) {
        super(n, def); this.modes = modes;
    }
    public void increment() {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equals(value)) { value = modes[(i + 1) % modes.length]; return; }
        }
    }
}
