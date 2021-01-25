package com.dpranantha.coroutineinterops.cache.model;

public class ProductCatalog {
    private final String productId;
    private final String productName;

    public ProductCatalog(String productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }
}
