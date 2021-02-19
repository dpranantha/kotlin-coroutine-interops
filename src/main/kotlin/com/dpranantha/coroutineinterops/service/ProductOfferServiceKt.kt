package com.dpranantha.coroutineinterops.service

import com.dpranantha.coroutineinterops.adapter.DataProvider
import com.dpranantha.coroutineinterops.cache.model.ProductOffer
import com.dpranantha.coroutineinterops.cache.model.ProductOffers
import com.dpranantha.coroutineinterops.lib.unwrap
import com.dpranantha.coroutineinterops.lib.wrap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.future
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
open class ProductOfferServiceKt(private val dataProvider: DataProvider) {
    suspend fun getProductOffers(productId: String): List<ProductOffer> = coroutineScope {
        dataProvider.getProductOffer(productId).unwrap()?.productOffers.orEmpty()
    }

    fun getProductOffersForJavaCall(productId: String): CompletableFuture<List<ProductOffer>> = GlobalScope.future {
        getProductOffers(productId)
    }
}
