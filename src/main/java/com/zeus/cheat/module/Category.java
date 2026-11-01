package com.zeus.cheat.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PVE("PvE"),
    VISUALS("Visuals"),
    WORLD("World"),
    RENDER("Render"),
    PLAYER("Player"),
    MISC("Misc");

    public final String displayName;
    
    Category(String d) { 
        this.displayName = d; 
    }
}
