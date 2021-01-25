package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.cache.model.ProductDescription;
import com.dpranantha.coroutineinterops.cache.model.ProductReview;
import com.dpranantha.coroutineinterops.cache.model.Seller;
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException;
import com.dpranantha.coroutineinterops.cache.model.ProductCatalogue;
import com.dpranantha.coroutineinterops.model.ProductOfferAndSeller;
import com.dpranantha.coroutineinterops.model.ProductSummary;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AggregatorService {
    private final ProductCatalogueService productCatalogueService;
    private final ProductDescriptionService productDescriptionService;
    private final ProductOfferService offerService;
    private final SellerService sellerService;
    private final ProductReviewService reviewService;

    @Autowired
    public AggregatorService(ProductCatalogueService productCatalogueService,
                             ProductDescriptionService productDescriptionService,
                             ProductOfferService offerService,
                             SellerService sellerService,
                             ProductReviewService reviewService) {
        this.productCatalogueService = productCatalogueService;
        this.productDescriptionService = productDescriptionService;
        this.offerService = offerService;
        this.sellerService = sellerService;
        this.reviewService = reviewService;
    }

    public ProductSummary getProductSummary(String productId) throws ProductNotFoundException {
        final ProductCatalogue productCatalogue = productCatalogueService.getProductInfo(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product can't be found!"));
        final Optional<ProductDescription> productDescription = productDescriptionService.getProductDescription(productId);
        final List<ProductOfferAndSeller> productOfferAndSellers = getProductOfferAndSellers(productId);
        final Pair<List<String>, Double> productReviews = getProductReviews(productId);
        return new ProductSummary(productId,
                productCatalogue.getProductName(),
                productDescription.map(ProductDescription::getShortDescription).orElse(null),
                productDescription.map(ProductDescription::getWeightInKg).orElse(null),
                productDescription.map(ProductDescription::getColor).orElse(null),
                productOfferAndSellers,
                productReviews.getLeft(),
                productReviews.getRight()
        );
    }

    private List<ProductOfferAndSeller> getProductOfferAndSellers(String productId) {
        return offerService.getProductOffers(productId).stream()
                .map(productOffer -> {
                    final Optional<Seller> seller = sellerService.getSeller(productOffer.getSellerId());
                    return new ProductOfferAndSeller(productOffer.getPrice(), seller.map(Seller::getSellerName).orElse(null));
                })
                .collect(Collectors.toList());
    }

    private Pair<List<String>, Double> getProductReviews(String productId) {
        final List<ProductReview> reviews = reviewService.getReviews(productId);
        final List<String> allReviews = new ArrayList<>();
        double rating = 0.0;
        for (ProductReview review: reviews) {
            allReviews.add(review.getReviewNote());
            rating += review.getStar();
        }
        if (reviews.size() != 0) rating = rating / reviews.size();
        return Pair.of(allReviews, rating);
    }
}
