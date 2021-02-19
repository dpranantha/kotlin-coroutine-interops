package com.dpranantha.coroutineinterops.service

import com.dpranantha.coroutineinterops.adapter.DataProvider
import com.dpranantha.coroutineinterops.cache.model.Seller
import com.dpranantha.coroutineinterops.lib.unwrap
import com.dpranantha.coroutineinterops.lib.wrap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.future
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
open class SellerServiceKt(private val dataProvider: DataProvider) {
    suspend fun getSeller(sellerId: String): Seller? = coroutineScope {
        dataProvider.getSeller(sellerId).unwrap()
    }

    fun getSellerForJavaCall(sellerId: String): CompletableFuture<Optional<Seller>> = GlobalScope.future {
        getSeller(sellerId).wrap()
    }
}
