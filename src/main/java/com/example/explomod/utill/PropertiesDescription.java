package com.example.explomod.utill;

public class PropertiesDescription {
    private final String name;
    private final String description;

    public PropertiesDescription(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PropertiesDescription){
            return obj==this;
        } else if (obj instanceof String) {
            return obj==name;
        }else{
            return false;
        }
    }
}
