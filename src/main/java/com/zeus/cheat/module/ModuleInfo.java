package com.zeus.cheat.module;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleInfo {
    String name();
    Category category();
    int key() default 0;
    String description() default "";
}
