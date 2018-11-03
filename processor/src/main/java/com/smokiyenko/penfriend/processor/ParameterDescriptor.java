package com.smokiyenko.penfriend.processor;

import javax.lang.model.type.TypeMirror;

public class ParameterDescriptor {

    private String name;
    private TypeMirror type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeMirror getType() {
        return type;
    }

    public void setType(TypeMirror type) {
        this.type = type;
    }
}
