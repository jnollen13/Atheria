package com.example.explomod.registries;

import jdk.jfr.Experimental;

@Experimental
public class BooleanLevelScaling extends LevelScaling{
    private final boolean b;

    public BooleanLevelScaling(boolean b) {
        super();
        this.b=b;
    }

    @Override
    public saveMethod getMethod() {
        return saveMethod.OTHER;
    }
}
