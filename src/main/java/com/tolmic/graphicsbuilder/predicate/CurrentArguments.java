package com.tolmic.graphicsbuilder.predicate;

import java.util.HashMap;

public class CurrentArguments {
    private double x;

    private HashMap<String, Double> parameters = new HashMap<>();

    public void clearParameters() {
        parameters.clear();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Double getParameterValue(String param) {
        return parameters.get(param);
    }

    public void addParameter(String param, double value) {
        parameters.put(param, value);
    }
}
