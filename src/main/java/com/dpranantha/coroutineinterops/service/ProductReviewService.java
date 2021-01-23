package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.adapter.DataProvider;
import com.dpranantha.coroutineinterops.cache.model.ProductReview;
import com.dpranantha.coroutineinterops.cache.model.ProductReviews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
class ProductReviewService {
    private final DataProvider dataProvider;

    @Autowired
    ProductReviewService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<ProductReview> getReviews(String productId) {
        return dataProvider.getProductReviews(productId)
                .map(ProductReviews::getProductReviews)
                .orElse(Collections.emptyList());
    }
}
