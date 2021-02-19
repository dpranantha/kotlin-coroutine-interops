package com.dpranantha.coroutineinterops.service

import com.dpranantha.coroutineinterops.adapter.DataProvider
import com.dpranantha.coroutineinterops.cache.model.ProductReview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.future
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
open class ProductReviewServiceKt(private val dataProvider: DataProvider) {
    suspend fun getProductReviews(productId: String): List<ProductReview> = coroutineScope {
        dataProvider.getProductReviews(productId).map { it.productReviews }.orElse(emptyList())
    }

    fun getProductReviewsForJavaCall(productId: String): CompletableFuture<List<ProductReview>> = GlobalScope.future {
        getProductReviews(productId)
    }
}
