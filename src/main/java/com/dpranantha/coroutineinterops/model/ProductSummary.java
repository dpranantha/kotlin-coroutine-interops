package com.dpranantha.coroutineinterops.model;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class ProductSummary implements Serializable {
    private final String productId;
    private final String productName;
    private final Optional<String> productDescription;
    private final Optional<Double> productWeightInKg;
    private final Optional<String> productColor;
    private final List<ProductOfferAndSeller> productOfferAndSellers;
    private final List<String> productReviews;
    private final double rating;

    public ProductSummary(String productId,
                          String productName,
                          String productDescription,
                          Double productWeightInKg,
                          String productColor,
                          List<ProductOfferAndSeller> productOfferAndSellers,
                          List<String> productReviews,
                          double rating) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = Optional.ofNullable(productDescription);
        this.productWeightInKg = Optional.ofNullable(productWeightInKg);
        this.productColor = Optional.ofNullable(productColor);
        this.productOfferAndSellers = productOfferAndSellers;
        this.productReviews = productReviews;
        this.rating = rating;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Optional<String> getProductDescription() {
        return productDescription;
    }

    public Optional<Double> getProductWeightInKg() {
        return productWeightInKg;
    }

    public Optional<String> getProductColor() {
        return productColor;
    }

    public List<ProductOfferAndSeller> getProductOfferAndSellers() {
        return productOfferAndSellers;
    }

    public List<String> getProductReviews() {
        return productReviews;
    }

    public double getRating() {
        return rating;
    }
}
