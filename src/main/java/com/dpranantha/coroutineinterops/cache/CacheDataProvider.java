package com.dpranantha.coroutineinterops.cache;

import com.dpranantha.coroutineinterops.adapter.DataProvider;
import com.dpranantha.coroutineinterops.cache.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@Qualifier("CacheDataProvider")
public class CacheDataProvider implements DataProvider {
    private static final Logger logger = LoggerFactory.getLogger(CacheDataProvider.class);
    public static final int DELAY = 200;

    private final CacheManager cacheManager;

    @Autowired
    public CacheDataProvider(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Optional<ProductInfo> getProductInfo(String productId) {
        logger.debug("Get product info from service for productId: {}", productId);
        try {
            TimeUnit.MILLISECONDS.sleep(DELAY);
            return getValueFromCache("BasicProduct", productId)
                    .map(value -> (ProductInfo) value);
        } catch (InterruptedException ie) {
            logger.error("Get product info from service for productId {} failed {}", productId, ie.getCause().getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProductDescription> getProductDescription(String productId) {
        logger.debug("Get product description from service for productId: {}", productId);
        try {
            TimeUnit.MILLISECONDS.sleep(DELAY);
            return getValueFromCache("Description", productId)
                    .map(value -> (ProductDescription) value);
        } catch (InterruptedException ie) {
            logger.error("Get product description from service for productId {} failed {}", productId, ie.getCause().getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProductOffers> getProductOffer(String productId) {
        logger.debug("Get product offers from service for productId: {}", productId);
        try {
            TimeUnit.MILLISECONDS.sleep(DELAY);
            return getValueFromCache("Offer", productId)
                    .map(value -> (ProductOffers) value);
        } catch (InterruptedException ie) {
            logger.error("Get product offers from service for productId {} failed {}", productId, ie.getCause().getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProductReviews> getProductReviews(String productId) {
        logger.debug("Get product reviews from service for productId: {}", productId);
        try {
            TimeUnit.MILLISECONDS.sleep(DELAY);
            return getValueFromCache("Review", productId)
                    .map(value -> (ProductReviews) value);
        } catch (InterruptedException ie) {
            logger.error("Get product reviews from service for productId {} failed {}", productId, ie.getCause().getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Seller> getSeller(String sellerId) {
        logger.debug("Get seller from service for productId: {}", sellerId);
        try {
            TimeUnit.MILLISECONDS.sleep(DELAY);
            return getValueFromCache("Seller", sellerId)
                    .map(value -> (Seller) value);
        } catch (InterruptedException ie) {
            logger.error("Get seller info from service for productId {} failed {}", sellerId, ie.getCause().getLocalizedMessage());
            return Optional.empty();
        }
    }

    private Optional<Object> getValueFromCache(String cacheName, String key) {
        return Optional.ofNullable(cacheManager.getCache(cacheName))
                .map(cache -> cache.get(key))
                .map(Cache.ValueWrapper::get);
    }
}
