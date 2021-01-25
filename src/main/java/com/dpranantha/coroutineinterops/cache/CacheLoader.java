package com.dpranantha.coroutineinterops.cache;

import com.dpranantha.coroutineinterops.cache.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CacheLoader {
    private static final List<String> colors = Arrays.asList("red", "green", "blue", "yellow", "black", "grey");
    private static final List<Double> weight = Arrays.asList(1.0, 2.0, 1.5, 3.0, 5.0, 2.5);
    private static final List<Double> prices = Arrays.asList(22.99, 12.99, 155.99, 98.99, 74.99, 45.99);
    private static final List<String> reviewNotes = Arrays.asList("ok", "good", "awesome");
    private static final List<Integer> star = Arrays.asList(3, 4, 5);
    private final CacheManager cacheManager;

    @Autowired
    public CacheLoader(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void loadProductInfoCache() {
        final Cache cache = this.cacheManager.getCache("BasicProduct");
        if (cache != null) {
            IntStream.range(0, 10).forEach(i -> {
                final String id = Integer.toString(i + 1);
                cache.put(id, new ProductCatalogue(id, String.format("Product %s", id)));
            });
        } else {
            throw new RuntimeException("BasicProduct cache should be initialized");
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void loadDescriptionCache() {
        final Cache cache = this.cacheManager.getCache("Description");
        if (cache != null) {
            IntStream.range(0, 10).forEach(i -> {
                final String id = Integer.toString(i + 1);
                cache.put(id, new ProductDescription(id,
                        String.format("This is product %s", id),
                        weight.get(i % weight.size()),
                        colors.get(i % colors.size())));
            });
        } else {
            throw new RuntimeException("Description cache should be initialized");
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void loadSellerCache() {
        final Cache cache = this.cacheManager.getCache("Seller");
        if (cache != null) {
            IntStream.range(0, 2).forEach(i -> {
                final String id = Integer.toString(i + 1);
                cache.put(id, new Seller(id,
                        String.format("Seller %s", id)));
            });
        } else {
            throw new RuntimeException("Seller cache should be initialized");
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void loadOfferCache() {
        final Cache cache = this.cacheManager.getCache("Offer");
        if (cache != null) {
            IntStream.range(0, 10).forEach(i -> {
                final String productId = Integer.toString(i + 1);
                final List<ProductOffer> productOffers = IntStream.range(0, 2).mapToObj(j -> {
                    final String offerId = Integer.toString(j + 1);
                    //sellerId same as offerId for simplicity
                    return new ProductOffer(offerId,
                            prices.get(j % prices.size()),
                            offerId);
                }).collect(Collectors.toList());
                cache.put(productId, new ProductOffers(productId, productOffers));
            });
        } else {
            throw new RuntimeException("Offer cache should be initialized");
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void loadReviewCache() {
        final Cache cache = this.cacheManager.getCache("Review");
        if (cache != null) {
            IntStream.range(0, 10).forEach(i -> {
                final String productId = Integer.toString(i);
                final List<ProductReview> productReviews = IntStream.range(0, 3).mapToObj(j -> {
                    final String reviewId = Integer.toString(j + 1);
                    return new ProductReview(reviewId,
                            String.format("Reviewer %s %s", reviewId, productId),
                            reviewNotes.get(j % reviewNotes.size()),
                            star.get(j % star.size()));
                }).collect(Collectors.toList());
                cache.put(productId, new ProductReviews(productId, productReviews));
            });
        } else {
            throw new RuntimeException("Review cache should be initialized");
        }
    }
}
