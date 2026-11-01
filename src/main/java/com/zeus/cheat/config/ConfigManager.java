package com.zeus.cheat.config;

import com.zeus.cheat.ZeusClient;
import com.zeus.cheat.module.Module;
import com.zeus.cheat.value.*;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class ConfigManager {
    private final Path dir = Paths.get("zeus");

    public void load() {
        try {
            Files.createDirectories(dir);
            Path cfg = dir.resolve("config.properties");
            if (!Files.exists(cfg)) return;
            Properties p = new Properties();
            try (InputStream in = Files.newInputStream(cfg)) { p.load(in); }
            for (Module m : ZeusClient.moduleManager.getModules()) {
                String prefix = m.getName() + ".";
                for (Value<?> v : m.getValues()) {
                    String raw = p.getProperty(prefix + v.getName());
                    if (raw == null) continue;
                    if (v instanceof BoolValue bv)   bv.set(Boolean.parseBoolean(raw));
                    if (v instanceof NumberValue nv) nv.set(Double.parseDouble(raw));
                    if (v instanceof ListValue lv)   lv.set(raw);
                }
                String en = p.getProperty(prefix + "enabled");
                if (en != null && Boolean.parseBoolean(en)) m.setEnabled(true);
            }
        } catch (Exception e) { ZeusClient.LOGGER.error("Config load failed", e); }
    }

    public void save() {
        try {
            Files.createDirectories(dir);
            Properties p = new Properties();
            for (Module m : ZeusClient.moduleManager.getModules()) {
                String prefix = m.getName() + ".";
                p.setProperty(prefix + "enabled", String.valueOf(m.isEnabled()));
                for (Value<?> v : m.getValues()) p.setProperty(prefix + v.getName(), String.valueOf(v.get()));
            }
            try (OutputStream out = Files.newOutputStream(dir.resolve("config.properties"))) {
                p.store(out, "Zeus config");
            }
        } catch (Exception e) { ZeusClient.LOGGER.error("Config save failed", e); }
    }
}
