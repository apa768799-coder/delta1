package com.zeus.cheat.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    WORLD("World"),
    MISC("Misc");

    public final String displayName;
    Category(String d) { this.displayName = d; }
}
