package com.dpranantha.coroutineinterops.service

import com.dpranantha.coroutineinterops.adapter.DataProvider
import com.dpranantha.coroutineinterops.cache.model.ProductDescription
import com.dpranantha.coroutineinterops.lib.unwrap
import com.dpranantha.coroutineinterops.lib.wrap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.future
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
open class ProductDescriptionServiceKt(private val dataProvider: DataProvider) {
    suspend fun getProductDescription(productId: String): ProductDescription? = coroutineScope {
        dataProvider.getProductDescription(productId).unwrap()
    }

    fun getProductDescriptionJavaCall(productId: String): CompletableFuture<Optional<ProductDescription>> = GlobalScope.future {
        getProductDescription(productId).wrap()
    }
}

