package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException;
import com.dpranantha.coroutineinterops.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AggregatorService {
    private final ProductService productService;
    private final DescriptionService descriptionService;
    private final OfferService offerService;
    private final SellerService sellerService;

    @Autowired
    public AggregatorService(ProductService productService,
                             DescriptionService descriptionService,
                             OfferService offerService,
                             SellerService sellerService) {
        this.productService = productService;
        this.descriptionService = descriptionService;
        this.offerService = offerService;
        this.sellerService = sellerService;
    }

    public Product getExtendedProductInfo(final String id) throws ProductNotFoundException {
        return productService.getProductBasicInfo();
    }
}
