package com.dpranantha.coroutineinterops.cache.model;

import java.util.List;

public class ProductOffers {
    private final String productId;
    private final List<ProductOffer> productOffers;

    public ProductOffers(String productId, List<ProductOffer> productOffers) {
        this.productId = productId;
        this.productOffers = productOffers;
    }

    public String getProductId() {
        return productId;
    }

    public List<ProductOffer> getProductOffers() {
        return productOffers;
    }
}
