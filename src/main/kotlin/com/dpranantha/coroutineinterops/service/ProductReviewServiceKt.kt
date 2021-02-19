package com.dpranantha.coroutineinterops.service

import com.dpranantha.coroutineinterops.adapter.DataProvider
import com.dpranantha.coroutineinterops.cache.model.ProductReviews
import com.dpranantha.coroutineinterops.lib.unwrap
import com.dpranantha.coroutineinterops.lib.wrap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.future
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
open class ProductReviewServiceKt(private val dataProvider: DataProvider) {
    suspend fun getProductReviews(productId: String): ProductReviews? = coroutineScope {
        dataProvider.getProductReviews(productId).unwrap()
    }

    fun getProductReviewsForJavaCall(productId: String): CompletableFuture<Optional<ProductReviews>> = GlobalScope.future {
        getProductReviews(productId).wrap()
    }
}
