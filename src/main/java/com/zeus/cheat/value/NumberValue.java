package com.zeus.cheat.value;

public class NumberValue extends Value<Number> {
    public final Number min, max;
    public NumberValue(String n, Number d, Number min, Number max) {
        super(n, d); this.min = min; this.max = max;
    }
    public void set(Number v) {
        double d = v.doubleValue();
        if (d < min.doubleValue()) d = min.doubleValue();
        if (d > max.doubleValue()) d = max.doubleValue();
        if (value instanceof Integer) value = (int) d;
        else if (value instanceof Float) value = (float) d;
        else value = d;
    }
}
