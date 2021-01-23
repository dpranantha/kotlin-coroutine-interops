package com.dpranantha.coroutineinterops.model;

import java.util.Optional;

public class ProductOfferAndSeller {
    private final double productPrice;
    private final Optional<String> sellerName;

    public ProductOfferAndSeller(double productPrice, String sellerName) {
        this.productPrice = productPrice;
        this.sellerName = Optional.ofNullable(sellerName);
    }

    public double getProductPrice() {
        return productPrice;
    }

    public Optional<String> getSellerName() {
        return sellerName;
    }
}
