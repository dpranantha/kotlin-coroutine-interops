package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.adapter.DataProvider;
import com.dpranantha.coroutineinterops.cache.model.ProductCatalogue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class ProductCatalogueService {
    private final DataProvider dataProvider;

    @Autowired
    ProductCatalogueService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Optional<ProductCatalogue> getProductInfo(String productId) {
        return dataProvider.getProductCatalogue(productId);
    }
}
