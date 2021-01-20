package com.dpranantha.coroutineinterops.model;

public class Offer {
    private final double price;
    private final Seller seller;

    public Offer(double price, Seller seller) {
        this.price = price;
        this.seller = seller;
    }

    public double getPrice() {
        return price;
    }

    public Seller getSeller() {
        return seller;
    }
}
