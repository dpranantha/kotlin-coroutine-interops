package com.dpranantha.coroutineinterops.model;

public class Description {
    private final String shortDescription;
    private final double weightInKg;
    private final String color;

    public Description(String shortDescription, double weightInKg, String color) {
        this.shortDescription = shortDescription;
        this.weightInKg = weightInKg;
        this.color = color;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public double getWeightInKg() {
        return weightInKg;
    }

    public String getColor() {
        return color;
    }
}
