package com.dpranantha.coroutineinterops.cache.model;

import java.util.List;

public class ProductReviews {
    private final String productId;
    private final List<ProductReview> productReviews;

    public ProductReviews(String productId, List<ProductReview> productReviews) {
        this.productId = productId;
        this.productReviews = productReviews;
    }

    public String getProductId() {
        return productId;
    }

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }
}
