package com.zeus.cheat.event;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    private final Map<Class<?>, List<ListenerEntry>> listeners = new HashMap<>();

    public void subscribe(Object target) {
        for (Method m : target.getClass().getDeclaredMethods()) {
            if (!m.isAnnotationPresent(EventListener.class)) continue;
            if (m.getParameterCount() != 1) continue;
            m.setAccessible(true);
            Class<?> eventType = m.getParameterTypes()[0];
            int priority = m.getAnnotation(EventListener.class).priority();
            listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                     .add(new ListenerEntry(target, m, priority));
            listeners.get(eventType).sort(Comparator.comparingInt(e -> -e.priority));
        }
    }

    public void unsubscribe(Object target) {
        for (List<ListenerEntry> list : listeners.values()) {
            list.removeIf(e -> e.target == target);
        }
    }

    public <T> T post(T event) {
        List<ListenerEntry> list = listeners.get(event.getClass());
        if (list == null) return event;
        for (ListenerEntry entry : list) {
            try {
                entry.method.invoke(entry.target, event);
                if (event instanceof CancellableEvent ce && ce.isCancelled()) break;
            } catch (Exception ignored) {}
        }
        return event;
    }

    private record ListenerEntry(Object target, Method method, int priority) {}
}
