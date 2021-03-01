package com.dpranantha.coroutineinterops.service

import com.dpranantha.coroutineinterops.cache.model.ProductCatalog
import com.dpranantha.coroutineinterops.cache.model.ProductDescription
import com.dpranantha.coroutineinterops.cache.model.ProductOffer
import com.dpranantha.coroutineinterops.cache.model.ProductReview
import com.dpranantha.coroutineinterops.cache.model.Seller
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*


class AggregatorServiceKtTests {
    private val mockProductCatalogServiceKt: ProductCatalogServiceKt = mockk()
    private val mockProductDescriptionServiceKt: ProductDescriptionServiceKt = mockk()
    private val mockOfferServiceKt: ProductOfferServiceKt = mockk()
    private val mockSellerServiceKt: SellerServiceKt = mockk()
    private val mockReviewServiceKt: ProductReviewServiceKt = mockk()

    private lateinit var aggregatorService: AggregatorServiceKt

    @BeforeEach
    fun setup() {
        aggregatorService = AggregatorServiceKt(
            mockProductCatalogServiceKt,
            mockProductDescriptionServiceKt,
            mockOfferServiceKt,
            mockSellerServiceKt,
            mockReviewServiceKt
        )
    }

    @Test
    @Throws(ProductNotFoundException::class)
    fun givenAllValidData_ThenReturnsProductSummary() {
        coEvery { mockProductCatalogServiceKt.getProductCatalog(any()) } returns ProductCatalog("1", "razor x1")
        coEvery { mockProductDescriptionServiceKt.getProductDescription(any()) } returns ProductDescription("1", "this is a razor x1", 1.5, "silver")
        coEvery { mockOfferServiceKt.getProductOffers(any()) } returns listOf(
                    ProductOffer("1", 20.0, "s-1"),
                    ProductOffer("2", 19.9, "s-2")
            )
        coEvery { mockSellerServiceKt.getSeller("s-1") } returns Seller("s-1", "expensive seller")
        coEvery { mockSellerServiceKt.getSeller("s-2") } returns Seller("s-2", "just a seller")
        coEvery { mockReviewServiceKt.getProductReviews(any()) }  returns listOf(
                ProductReview("1", "anonymous", "that is awesome", 5),
                ProductReview("2", "mr. A", "that is ok", 3)
            )

        val productSummary = runBlocking { aggregatorService.getProductSummary("1") }

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
        coEvery { mockProductCatalogServiceKt.getProductCatalog(any()) } returns null

        Assertions.assertThrows(ProductNotFoundException::class.java) {
            runBlocking {
                aggregatorService.getProductSummary("1")
            }
        }
    }

    @Test
    @Throws(ProductNotFoundException::class)
    fun givenOnlyValidProductCatalogAndOfferPriceData_ThenReturnsProductSummary() {
        coEvery { mockProductCatalogServiceKt.getProductCatalog(any()) } returns ProductCatalog("1", "razor x1")
        coEvery { mockProductDescriptionServiceKt.getProductDescription(any()) } returns null
        coEvery { mockOfferServiceKt.getProductOffers(any()) } returns  listOf(ProductOffer("1", 20.0, "s-1"))
        coEvery { mockSellerServiceKt.getSeller("s-1") } returns null
        coEvery { mockReviewServiceKt.getProductReviews(any()) } returns emptyList()

        val productSummary = runBlocking { aggregatorService.getProductSummary("1") }

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
