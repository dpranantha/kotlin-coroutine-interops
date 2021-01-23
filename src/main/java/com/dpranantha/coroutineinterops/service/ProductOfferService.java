package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.adapter.DataProvider;
import com.dpranantha.coroutineinterops.cache.model.ProductOffer;
import com.dpranantha.coroutineinterops.cache.model.ProductOffers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
class ProductOfferService {
    private final DataProvider dataProvider;

    @Autowired
    ProductOfferService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<ProductOffer> getProductOffers(String productId) {
        return dataProvider.getProductOffer(productId)
                .map(ProductOffers::getProductOffers)
                .orElse(Collections.emptyList());
    }
}
