package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.adapter.DataProvider;
import com.dpranantha.coroutineinterops.cache.model.ProductDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class ProductDescriptionService {
    private final DataProvider dataProvider;

    @Autowired
    ProductDescriptionService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Optional<ProductDescription> getProductDescription(String productId) {
        return dataProvider.getProductDescription(productId);
    }
}
