package com.dpranantha.coroutineinterops.service

import com.dpranantha.coroutineinterops.adapter.DataProvider
import com.dpranantha.coroutineinterops.cache.model.ProductCatalog
import com.dpranantha.coroutineinterops.lib.unwrap
import com.dpranantha.coroutineinterops.lib.wrap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.future
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
open class ProductCatalogServiceKt(private val dataProvider: DataProvider) {
    suspend fun getProductCatalog(productId: String): ProductCatalog? = coroutineScope {
        dataProvider.getProductCatalog(productId).unwrap()
    }

    fun getProductCatalogJavaCall(productId: String): CompletableFuture<Optional<ProductCatalog>> = GlobalScope.future {
        getProductCatalog(productId).wrap()
    }
}
