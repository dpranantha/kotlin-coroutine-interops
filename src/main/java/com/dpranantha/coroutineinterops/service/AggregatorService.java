package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.cache.model.*;
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException;
import com.dpranantha.coroutineinterops.model.ProductOfferAndSeller;
import com.dpranantha.coroutineinterops.model.ProductSummary;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AggregatorService {
    private static final Logger logger = LoggerFactory.getLogger(AggregatorService.class);

    private final ProductCatalogService productCatalogService;
    private final ProductDescriptionService productDescriptionService;
    private final ProductOfferService offerService;
    private final SellerService sellerService;
    private final ProductReviewService reviewService;
    private final ProductCatalogServiceKt productCatalogServiceKt;
    private final ProductDescriptionServiceKt productDescriptionServiceKt;
    private final ProductOfferServiceKt offerServiceKt;
    private final SellerServiceKt sellerServiceKt;
    private final ProductReviewServiceKt reviewServiceKt;
    private final boolean useKotlin;

    @Autowired
    public AggregatorService(ProductCatalogService productCatalogService,
                             ProductDescriptionService productDescriptionService,
                             ProductOfferService offerService,
                             SellerService sellerService,
                             ProductReviewService reviewService,
                             ProductCatalogServiceKt productCatalogServiceKt,
                             ProductDescriptionServiceKt productDescriptionServiceKt,
                             ProductOfferServiceKt offerServiceKt,
                             SellerServiceKt sellerServiceKt,
                             ProductReviewServiceKt reviewServiceKt,
                             @Value("${use.kotlin:false}") boolean useKotlin) {
        this.productCatalogService = productCatalogService;
        this.productDescriptionService = productDescriptionService;
        this.offerService = offerService;
        this.sellerService = sellerService;
        this.reviewService = reviewService;
        this.productCatalogServiceKt = productCatalogServiceKt;
        this.productDescriptionServiceKt = productDescriptionServiceKt;
        this.offerServiceKt = offerServiceKt;
        this.sellerServiceKt = sellerServiceKt;
        this.reviewServiceKt = reviewServiceKt;
        this.useKotlin = useKotlin;
    }

    public ProductSummary getProductSummary(String productId) throws ProductNotFoundException {
        final ProductCatalog productCatalog = productCatalogService.getProductCatalog(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product can't be found!"));
        final CompletableFuture<Optional<ProductDescription>> productDescriptionAsync = getProductDescriptionReroute(productId)
                .exceptionally(t -> {
                    logger.error("Error retrieving data for product description {}", t.getCause().getLocalizedMessage());
                    return Optional.empty();
                });
        final CompletableFuture<List<ProductOfferAndSeller>> productOfferAndSellersAsync = getProductOfferAndSellersReroute(productId);
        final Pair<List<String>, Double> productReviews = getProductReviews(productId);
        final Optional<ProductDescription> productDescription = productDescriptionAsync.join();
        final List<ProductOfferAndSeller> productOfferAndSellers = productOfferAndSellersAsync.join();
        return new ProductSummary(productId,
                productCatalog.getProductName(),
                productDescription.map(ProductDescription::getShortDescription).orElse(null),
                productDescription.map(ProductDescription::getWeightInKg).orElse(null),
                productDescription.map(ProductDescription::getColor).orElse(null),
                productOfferAndSellers,
                productReviews.getLeft(),
                productReviews.getRight());
    }

    private CompletableFuture<Optional<ProductDescription>> getProductDescriptionReroute(String productId) {
        if (useKotlin) {
            return productDescriptionServiceKt.getProductDescriptionJavaCall(productId);
        } else  {
            return CompletableFuture.supplyAsync(() -> productDescriptionService.getProductDescription(productId));
        }
    }

    private CompletableFuture<List<ProductOfferAndSeller>> getProductOfferAndSellersReroute(String productId) {
        if (useKotlin) {
            return offerServiceKt.getProductOffersForJavaCall(productId)
                    .exceptionally(t -> {
                        logger.error("Error retrieving data for product offer with productId {}: {}", productId, t.getCause().getLocalizedMessage());
                        return Collections.emptyList();
                    })
                    .thenCompose(productOffers -> {
                        final List<CompletableFuture<ProductOfferAndSeller>> productOfferAndSellerFutures = productOffers.stream()
                                .map(productOffer -> sellerServiceKt.getSellerForJavaCall(productOffer.getSellerId())
                                        .exceptionally(t -> {
                                            logger.error("Error retrieving data for seller with sellerId {}: {}", productOffer.getSellerId(), t.getCause().getLocalizedMessage());
                                            return Optional.empty();
                                        })
                                        .thenApply(seller -> new ProductOfferAndSeller(productOffer.getPrice(), seller.map(Seller::getSellerName).orElse(null))))
                                .collect(Collectors.toList());
                        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(productOfferAndSellerFutures.toArray(new CompletableFuture[0]));
                        return allDoneFuture.thenApply(v -> productOfferAndSellerFutures.stream()
                                .map(CompletableFuture::join).
                                collect(Collectors.toList()));
                    });
        } else {
            return CompletableFuture.supplyAsync(() -> offerService.getProductOffers(productId).stream()
                    .map(productOffer -> {
                        final Optional<Seller> seller = sellerService.getSeller(productOffer.getSellerId());
                        return new ProductOfferAndSeller(productOffer.getPrice(), seller.map(Seller::getSellerName).orElse(null));
                    })
                    .collect(Collectors.toList()));
        }
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
