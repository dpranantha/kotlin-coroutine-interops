package com.dpranantha.coroutineinterops.cache.model;

public class ProductOffer {
    private final String offerId;
    private final double price;
    private final String sellerId;

    public ProductOffer(String offerId, double price, String sellerId) {
        this.offerId = offerId;
        this.price = price;
        this.sellerId = sellerId;
    }

    public String getOfferId() {
        return offerId;
    }

    public double getPrice() {
        return price;
    }

    public String getSellerId() {
        return sellerId;
    }
}
