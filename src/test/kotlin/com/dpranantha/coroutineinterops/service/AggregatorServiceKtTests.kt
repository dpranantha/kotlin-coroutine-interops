package com.dpranantha.coroutineinterops.service

import com.dpranantha.coroutineinterops.cache.model.ProductCatalog
import com.dpranantha.coroutineinterops.cache.model.ProductDescription
import com.dpranantha.coroutineinterops.cache.model.ProductOffer
import com.dpranantha.coroutineinterops.cache.model.ProductReview
import com.dpranantha.coroutineinterops.cache.model.Seller
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import java.util.*
import java.util.concurrent.CompletableFuture


class AggregatorServiceKtTests {
    private val mockProductCatalogService: ProductCatalogService = mockk()
    private val mockProductDescriptionService: ProductDescriptionService = mockk()
    private val mockOfferService: ProductOfferService = mockk()
    private val mockSellerService: SellerService = mockk()
    private val mockReviewService: ProductReviewService = mockk()
    private val mockProductDescriptionServiceKt: ProductDescriptionServiceKt = mockk()

    private lateinit var aggregatorService: AggregatorService

    @BeforeEach
    fun setup() {
        aggregatorService = AggregatorService(
            mockProductCatalogService,
            mockProductDescriptionService,
            mockOfferService,
            mockSellerService,
            mockReviewService,
            mockProductDescriptionServiceKt,
            true
        )
    }

    @Test
    @Throws(ProductNotFoundException::class)
    fun givenAllValidData_ThenReturnsProductSummary() {
        every { mockProductCatalogService.getProductCatalog(any()) } returns Optional.of(ProductCatalog("1", "razor x1"))
        every { mockProductDescriptionServiceKt.getProductDescriptionJavaCall(any()) } returns CompletableFuture.supplyAsync { Optional.of(ProductDescription("1", "this is a razor x1", 1.5, "silver")) }
        every { mockOfferService.getProductOffers(any()) } returns listOf(
                    ProductOffer("1", 20.0, "s-1"),
                    ProductOffer("2", 19.9, "s-2")
                )
        every { mockSellerService.getSeller("s-1") } returns Optional.of(Seller("s-1", "expensive seller"))
        every { mockSellerService.getSeller("s-2") } returns Optional.of(Seller("s-2", "just a seller"))
        every { mockReviewService.getReviews(any()) }  returns listOf(
                    ProductReview("1", "anonymous", "that is awesome", 5),
                    ProductReview("2", "mr. A", "that is ok", 3)
                )

        val productSummary = aggregatorService.getProductSummary("1")

        Assertions.assertEquals("razor x1", productSummary.productName)
        Assertions.assertEquals("silver", productSummary.productColor.get())
        Assertions.assertEquals("this is a razor x1", productSummary.productDescription.get())
        Assertions.assertEquals(1.5, productSummary.productWeightInKg.get())
        Assertions.assertEquals(2, productSummary.productOfferAndSellers.size)
        Assertions.assertEquals("expensive seller", productSummary.productOfferAndSellers[0].sellerName.get())
        Assertions.assertEquals(20.0, productSummary.productOfferAndSellers[0].productPrice)
        Assertions.assertEquals("just a seller", productSummary.productOfferAndSellers[1].sellerName.get())
        Assertions.assertEquals(19.9, productSummary.productOfferAndSellers[1].productPrice)
        Assertions.assertEquals(2, productSummary.productReviews.size)
        Assertions.assertEquals(Arrays.asList("that is awesome", "that is ok"), productSummary.productReviews)
        Assertions.assertEquals(4.0, productSummary.rating)
    }

    @Test
    @Throws(ProductNotFoundException::class)
    fun givenNotFoundProduct_ThrowsException() {
        every { mockProductCatalogService.getProductCatalog(any()) } returns Optional.empty()

        Assertions.assertThrows(ProductNotFoundException::class.java) { aggregatorService.getProductSummary("1") }
    }

    @Test
    @Throws(ProductNotFoundException::class)
    fun givenOnlyValidProductCatalogAndOfferPriceData_ThenReturnsProductSummary() {
        every { mockProductCatalogService.getProductCatalog(any()) } returns Optional.of(ProductCatalog("1", "razor x1"))
        every { mockProductDescriptionServiceKt.getProductDescriptionJavaCall(any()) } returns CompletableFuture.supplyAsync { Optional.empty() }
        every { mockOfferService.getProductOffers(any()) } returns listOf(ProductOffer("1", 20.0, "s-1"))
        every { mockSellerService.getSeller("s-1") } returns Optional.empty()
        every { mockReviewService.getReviews(any()) } returns emptyList()

        val productSummary = aggregatorService.getProductSummary("1")

        Assertions.assertEquals("razor x1", productSummary.productName)
        Assertions.assertFalse(productSummary.productColor.isPresent)
        Assertions.assertFalse(productSummary.productDescription.isPresent)
        Assertions.assertFalse(productSummary.productWeightInKg.isPresent)
        Assertions.assertEquals(1, productSummary.productOfferAndSellers.size)
        Assertions.assertFalse(productSummary.productOfferAndSellers[0].sellerName.isPresent)
        Assertions.assertEquals(20.0, productSummary.productOfferAndSellers[0].productPrice)
        Assertions.assertTrue(productSummary.productReviews.isEmpty())
        Assertions.assertEquals(0.0, productSummary.rating)
    }
}
