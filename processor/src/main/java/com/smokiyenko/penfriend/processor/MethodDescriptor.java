package com.smokiyenko.penfriend.processor;

import java.util.List;

public class MethodDescriptor {

    private String methodName;
    private List<ParameterDescriptor> parameters;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<ParameterDescriptor> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterDescriptor> parameters) {
        this.parameters = parameters;
    }
}
