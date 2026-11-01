package com.zeus.cheat.module;

import com.zeus.cheat.module.impl.combat.*;
import com.zeus.cheat.module.impl.movement.*;
import com.zeus.cheat.module.impl.render.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        register(new KillAura());
        register(new Velocity());
        register(new Fly());
        register(new Speed());
        register(new ESP());
        register(new FullBright());
    }

    private void register(Module m) { modules.add(m); }

    public List<Module> getModules() { return modules; }

    public List<Module> getModulesByCategory(Category c) {
        return modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
    }

    public Module getByName(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
