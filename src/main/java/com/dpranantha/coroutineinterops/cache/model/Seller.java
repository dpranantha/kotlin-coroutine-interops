package com.dpranantha.coroutineinterops.cache.model;

public class Seller {
    private final String sellerId;
    private final String sellerName;

    public Seller(String sellerId, String sellerName) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }
}
