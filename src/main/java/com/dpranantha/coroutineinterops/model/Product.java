package com.dpranantha.coroutineinterops.model;

import java.util.List;

public class Product {
    private final String id;
    private final String name;
    private final Description description;
    private final List<Offer> offers;
    private final List<Review> reviews;

    public Product(String id, String name, Description description, List<Offer> offers, List<Review> reviews) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.offers = offers;
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
