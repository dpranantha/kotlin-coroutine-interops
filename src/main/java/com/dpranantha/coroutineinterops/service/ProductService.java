package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException;
import com.dpranantha.coroutineinterops.model.Product;
import org.springframework.stereotype.Service;

@Service
class ProductService {
    public Product getProductBasicInfo() throws ProductNotFoundException {
        throw new ProductNotFoundException("Service is under construction");
    }
}
