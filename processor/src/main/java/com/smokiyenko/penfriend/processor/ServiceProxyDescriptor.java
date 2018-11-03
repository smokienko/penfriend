package com.smokiyenko.penfriend.processor;

import java.util.List;

import javax.lang.model.type.TypeMirror;

public class ServiceProxyDescriptor {

    private String className;
    private String packageName;
    private TypeMirror parent;
    private List<MethodDescriptor> methods;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<MethodDescriptor> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodDescriptor> methods) {
        this.methods = methods;
    }

    public TypeMirror getParent() {
        return parent;
    }

    public void setParent(TypeMirror parent) {
        this.parent = parent;
    }
}
