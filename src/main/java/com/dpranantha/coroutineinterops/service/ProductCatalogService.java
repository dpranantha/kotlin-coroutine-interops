package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.adapter.DataProvider;
import com.dpranantha.coroutineinterops.cache.model.ProductCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class ProductCatalogService {
    private final DataProvider dataProvider;

    @Autowired
    ProductCatalogService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Optional<ProductCatalog> getProductCatalog(String productId) {
        return dataProvider.getProductCatalog(productId);
    }
}
