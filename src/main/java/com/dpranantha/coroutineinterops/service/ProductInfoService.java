package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.adapter.DataProvider;
import com.dpranantha.coroutineinterops.cache.model.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class ProductInfoService {
    private final DataProvider dataProvider;

    @Autowired
    ProductInfoService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Optional<ProductInfo> getProductInfo(String productId) {
        return dataProvider.getProductInfo(productId);
    }
}
