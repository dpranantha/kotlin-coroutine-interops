package com.dpranantha.coroutineinterops.cache.model;

public class ProductDescription {
    private final String productId;
    private final String shortDescription;
    private final double weightInKg;
    private final String color;

    public ProductDescription(String productId, String shortDescription, double weightInKg, String color) {
        this.productId = productId;
        this.shortDescription = shortDescription;
        this.weightInKg = weightInKg;
        this.color = color;
    }

    public String getProductId() {
        return productId;
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
