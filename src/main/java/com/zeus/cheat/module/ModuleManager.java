package com.zeus.cheat.module;

import com.zeus.cheat.module.impl.combat.KillAura;
import com.zeus.cheat.module.impl.combat.Velocity;
import com.zeus.cheat.module.impl.movement.Fly;
import com.zeus.cheat.module.impl.movement.Speed;
import com.zeus.cheat.module.impl.pve.AutoArmor;
import com.zeus.cheat.module.impl.pve.AutoTotem;
import com.zeus.cheat.module.impl.render.ESP;
import com.zeus.cheat.module.impl.render.FullBright;
import com.zeus.cheat.module.impl.visuals.Nametags;
import com.zeus.cheat.module.impl.world.AutoMine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        // Combat
        register(new KillAura());
        register(new Velocity());

        // Movement
        register(new Fly());
        register(new Speed());

        // PvE
        register(new AutoTotem());
        register(new AutoArmor());

        // Visuals & Render
        register(new ESP());
        register(new FullBright());
        register(new Nametags());

        // World
        register(new AutoMine());
    }

    private void register(Module m) { 
        modules.add(m); 
    }

    public List<Module> getModules() { 
        return modules; 
    }

    public List<Module> getModulesByCategory(Category c) {
        return modules.stream()
                .filter(m -> m.getCategory() == c)
                .collect(Collectors.toList());
    }

    public Module getByName(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
