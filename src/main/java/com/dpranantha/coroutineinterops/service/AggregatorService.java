package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.cache.model.ProductDescription;
import com.dpranantha.coroutineinterops.cache.model.ProductReview;
import com.dpranantha.coroutineinterops.cache.model.Seller;
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException;
import com.dpranantha.coroutineinterops.cache.model.ProductCatalog;
import com.dpranantha.coroutineinterops.model.ProductOfferAndSeller;
import com.dpranantha.coroutineinterops.model.ProductSummary;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final ProductDescriptionServiceKt productDescriptionServiceKt;
    private final boolean useKotlin;

    @Autowired
    public AggregatorService(ProductCatalogService productCatalogService,
                             ProductDescriptionService productDescriptionService,
                             ProductOfferService offerService,
                             SellerService sellerService,
                             ProductReviewService reviewService,
                             ProductDescriptionServiceKt productDescriptionServiceKt,
                             @Value("${use.kotlin:false}") boolean useKotlin) {
        this.productCatalogService = productCatalogService;
        this.productDescriptionService = productDescriptionService;
        this.offerService = offerService;
        this.sellerService = sellerService;
        this.reviewService = reviewService;
        this.productDescriptionServiceKt = productDescriptionServiceKt;
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
        final List<ProductOfferAndSeller> productOfferAndSellers = getProductOfferAndSellers(productId);
        final Pair<List<String>, Double> productReviews = getProductReviews(productId);
        final Optional<ProductDescription> productDescription = productDescriptionAsync.join();
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
