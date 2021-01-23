package com.dpranantha.coroutineinterops.adapter;

import com.dpranantha.coroutineinterops.cache.model.*;

import java.util.Optional;

public interface DataProvider {
    Optional<ProductInfo> getProductInfo(String productId);
    Optional<ProductDescription> getProductDescription(String productId);
    Optional<ProductOffers> getProductOffer(String productId);
    Optional<ProductReviews> getProductReviews(String productId);
    Optional<Seller> getSeller(String sellerId);
}
