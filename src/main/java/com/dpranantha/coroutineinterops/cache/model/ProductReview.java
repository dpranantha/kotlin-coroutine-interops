package com.dpranantha.coroutineinterops.cache.model;

public class ProductReview {
    private final String reviewId;
    private final String reviewerName;
    private final String reviewNote;
    private final Integer star;

    public ProductReview(String reviewId, String reviewerName, String reviewNote, Integer star) {
        this.reviewId = reviewId;
        this.reviewerName = reviewerName;
        this.reviewNote = reviewNote;
        this.star = star;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getReviewNote() {
        return reviewNote;
    }

    public Integer getStar() {
        return star;
    }
}
