package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.adapter.DataProvider;
import com.dpranantha.coroutineinterops.cache.model.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class SellerService {
    private final DataProvider dataProvider;

    @Autowired
    SellerService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Optional<Seller> getSeller(String sellerId) {
        return dataProvider.getSeller(sellerId);
    }
}
