package com.dpranantha.coroutineinterops.model;

public class Review {
    private final String reviewerName;
    private final String reviewNote;
    private final Integer star;

    public Review(String reviewerName, String reviewNote, Integer star) {
        this.reviewerName = reviewerName;
        this.reviewNote = reviewNote;
        this.star = star;
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
