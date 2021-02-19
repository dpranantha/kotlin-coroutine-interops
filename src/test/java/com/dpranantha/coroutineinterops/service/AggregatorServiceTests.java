package com.dpranantha.coroutineinterops.service;

import com.dpranantha.coroutineinterops.cache.model.*;
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException;
import com.dpranantha.coroutineinterops.model.ProductSummary;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class AggregatorServiceTests {
    @Mock
    private ProductCatalogService mockProductCatalogService;
    @Mock
    private ProductDescriptionService mockProductDescriptionService;
    @Mock
    private ProductOfferService mockOfferService;
    @Mock
    private SellerService mockSellerService;
    @Mock
    private ProductReviewService mockReviewService;
    @Mock
    private ProductCatalogServiceKt mockProductCatalogServiceKt;
    @Mock
    private ProductDescriptionServiceKt mockProductDescriptionServiceKt;
    @Mock
    private ProductOfferServiceKt mockOfferServiceKt;
    @Mock
    private SellerServiceKt mockSellerServiceKt;
    @Mock
    private ProductReviewServiceKt mockReviewServiceKt;

    private AggregatorService aggregatorService;

    @Before
    public void setup(){
        aggregatorService = new AggregatorService(
                mockProductCatalogService,
                mockProductDescriptionService,
                mockOfferService,
                mockSellerService,
                mockReviewService,
                mockProductCatalogServiceKt,
                mockProductDescriptionServiceKt,
                mockOfferServiceKt,
                mockSellerServiceKt,
                mockReviewServiceKt,
                false);
    }

    @Test
    public void givenAllValidData_ThenReturnsProductSummary() throws ProductNotFoundException {
        Mockito.when(mockProductCatalogService.getProductCatalog(anyString()))
                .thenReturn(Optional.of(new ProductCatalog("1", "razor x1")));
        Mockito.when(mockProductDescriptionService.getProductDescription(anyString()))
                .thenReturn(Optional.of(new ProductDescription("1", "this is a razor x1", 1.5, "silver")));
        Mockito.when(mockOfferService.getProductOffers(anyString()))
                .thenReturn(Arrays.asList(new ProductOffer("1", 20.0, "s-1"),
                        new ProductOffer("2", 19.9, "s-2")));
        Mockito.when(mockSellerService.getSeller("s-1"))
                .thenReturn(Optional.of(new Seller("s-1", "expensive seller")));
        Mockito.when(mockSellerService.getSeller("s-2"))
                .thenReturn(Optional.of(new Seller("s-2", "just a seller")));
        Mockito.when(mockReviewService.getReviews(anyString()))
                .thenReturn(Arrays.asList(new ProductReview("1", "anonymous", "that is awesome", 5),
                        new ProductReview("2", "mr. A", "that is ok", 3)));

        final ProductSummary productSummary = aggregatorService.getProductSummary("1");
        Assertions.assertEquals("razor x1", productSummary.getProductName());
        Assertions.assertEquals("silver", productSummary.getProductColor().get());
        Assertions.assertEquals("this is a razor x1", productSummary.getProductDescription().get());
        Assertions.assertEquals(1.5, productSummary.getProductWeightInKg().get());
        Assertions.assertEquals(2, productSummary.getProductOfferAndSellers().size());
        Assertions.assertEquals("expensive seller", productSummary.getProductOfferAndSellers().get(0).getSellerName().get());
        Assertions.assertEquals(20.0, productSummary.getProductOfferAndSellers().get(0).getProductPrice());
        Assertions.assertEquals("just a seller", productSummary.getProductOfferAndSellers().get(1).getSellerName().get());
        Assertions.assertEquals(19.9, productSummary.getProductOfferAndSellers().get(1).getProductPrice());
        Assertions.assertEquals(2, productSummary.getProductReviews().size());
        Assertions.assertEquals(Arrays.asList("that is awesome", "that is ok"), productSummary.getProductReviews());
        Assertions.assertEquals(4.0, productSummary.getRating());
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNotFoundProduct_ThrowsException() throws ProductNotFoundException {
        Mockito.when(mockProductCatalogService.getProductCatalog(anyString()))
                .thenReturn(Optional.empty());

        aggregatorService.getProductSummary("1");
    }

    @Test
    public void givenOnlyValidProductCatalogAndOfferPriceData_ThenReturnsProductSummary() throws ProductNotFoundException {
        Mockito.when(mockProductCatalogService.getProductCatalog(anyString()))
                .thenReturn(Optional.of(new ProductCatalog("1", "razor x1")));
        Mockito.when(mockProductDescriptionService.getProductDescription(anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(mockOfferService.getProductOffers(anyString()))
                .thenReturn(Collections.singletonList(new ProductOffer("1", 20.0, "s-1")));
        Mockito.when(mockSellerService.getSeller("s-1"))
                .thenReturn(Optional.empty());
        Mockito.when(mockReviewService.getReviews(anyString()))
                .thenReturn(Collections.emptyList());

        final ProductSummary productSummary = aggregatorService.getProductSummary("1");
        Assertions.assertEquals("razor x1", productSummary.getProductName());
        Assertions.assertFalse(productSummary.getProductColor().isPresent());
        Assertions.assertFalse(productSummary.getProductDescription().isPresent());
        Assertions.assertFalse(productSummary.getProductWeightInKg().isPresent());
        Assertions.assertEquals(1, productSummary.getProductOfferAndSellers().size());
        Assertions.assertFalse(productSummary.getProductOfferAndSellers().get(0).getSellerName().isPresent());
        Assertions.assertEquals(20.0, productSummary.getProductOfferAndSellers().get(0).getProductPrice());
        Assertions.assertTrue(productSummary.getProductReviews().isEmpty());
        Assertions.assertEquals(0.0, productSummary.getRating());
    }
}
