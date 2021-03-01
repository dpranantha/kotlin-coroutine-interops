package com.dpranantha.coroutineinterops.service

import com.dpranantha.coroutineinterops.cache.model.ProductReview
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException
import com.dpranantha.coroutineinterops.model.ProductOfferAndSeller
import com.dpranantha.coroutineinterops.model.ProductSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.future
import kotlinx.coroutines.supervisorScope
import org.apache.commons.lang3.tuple.Pair
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class AggregatorServiceKt(
    private val productCatalogServiceKt: ProductCatalogServiceKt,
    private val productDescriptionServiceKt: ProductDescriptionServiceKt,
    private val productOfferServiceKt: ProductOfferServiceKt,
    private val sellerServiceKt: SellerServiceKt,
    private val productReviewServiceKt: ProductReviewServiceKt
) {

    @Throws(ProductNotFoundException::class)
    fun getProductSummaryForJavaCall(productId: String): CompletableFuture<ProductSummary> = GlobalScope.future { getProductSummary(productId) }

    @Throws(ProductNotFoundException::class)
    suspend fun getProductSummary(productId: String): ProductSummary = coroutineScope {
        val productCatalogDeferred = async { productCatalogServiceKt.getProductCatalog(productId) }
        val productCatalog = try {
            productCatalogDeferred.await()
        } catch (e: Exception) {
            logger.error("Error retrieving data for product catalog with productId {}: {}", productId, e.cause?.localizedMessage)
            null
        }
        if (productCatalog == null) throw ProductNotFoundException("Product can't be found!") //simplicity, throw Product Not Found
        else
            supervisorScope {
                val productDescriptionDeferred = async { productDescriptionServiceKt.getProductDescription(productId) }
                val productOffersDeferred = async { productOfferServiceKt.getProductOffers(productId) }
                val productReviewsDeferred = async { productReviewServiceKt.getProductReviews(productId) }

                val productDescription = try{
                    productDescriptionDeferred.await()
                } catch (e: Exception) {
                    logger.error("Error retrieving data for product description with productId {}: {}", productId, e.cause?.localizedMessage)
                    null
                }

                val productReviews = try {
                    getReviewsAndRating(productReviewsDeferred.await())
                } catch (e: Exception) {
                    logger.error("Error retrieving data for product review with productId {}: {}", productId, e.cause?.localizedMessage)
                    Pair.of(emptyList(), 0.0)
                }

                /* // Sequential map
                val productOfferAndSellers = try {
                    productOffersDeferred.await()
                        .map {
                            // Note: this is a sequential map, hence it is kind of useless to have coroutine implementation in here
                            supervisorScope {
                                val sellerDeferred = async(Dispatchers.IO) { sellerServiceKt.getSeller(it.sellerId) }
                                try {
                                    val seller = sellerDeferred.await()
                                    ProductOfferAndSeller(it.price, seller?.sellerName)
                                } catch (e: Exception) {
                                    logger.error("Error retrieving data for seller with sellerId {}: {}", it.sellerId, e.cause?.localizedMessage)
                                    ProductOfferAndSeller(it.price, null)
                                }
                            }
                        }
                        .toList()
                } catch (e: Exception) {
                    logger.error("Error retrieving data for product offer and seller with productId {}: {}", productId, e.cause?.localizedMessage)
                    emptyList()
                }
                */

                // Concurrent map
                val productOfferAndSellers = try {
                    productOffersDeferred.await()
                        .asFlow()
                        .concurrentMap(this, 5) {
                            supervisorScope {
                                val sellerDeferred = async(Dispatchers.IO) { sellerServiceKt.getSeller(it.sellerId) }
                                try {
                                    val seller = sellerDeferred.await()
                                    ProductOfferAndSeller(it.price, seller?.sellerName)
                                } catch (e: Exception) {
                                    logger.error("Error retrieving data for seller with sellerId {}: {}", it.sellerId, e.cause?.localizedMessage)
                                    ProductOfferAndSeller(it.price, null)
                                }
                            }
                        }
                        .toList()
                } catch (e: Exception) {
                    logger.error("Error retrieving data for product offer and seller with productId {}: {}", productId, e.cause?.localizedMessage)
                    emptyList()
                }

                ProductSummary(
                    productId,
                    productCatalog.productName,
                    productDescription?.shortDescription,
                    productDescription?.weightInKg,
                    productDescription?.color,
                    productOfferAndSellers,
                    productReviews.left,
                    productReviews.right)
            }
    }

    private fun getReviewsAndRating(reviews: List<ProductReview>): Pair<List<String>, Double> {
        val allReviews: MutableList<String> = ArrayList()
        var rating = 0.0
        for (review in reviews) {
            allReviews.add(review.reviewNote)
            rating += review.star.toDouble()
        }
        if (reviews.isNotEmpty()) rating /= reviews.size
        return Pair.of(allReviews, rating)
    }

    private fun <T, R> Flow<T>.concurrentMap(scope: CoroutineScope, concurrencyLevel: Int, transform: suspend (T) -> R): Flow<R> = this
        .map { scope.async { transform(it) } }
        .buffer(concurrencyLevel)
        .map { it.await() }

    companion object {
        private val logger = LoggerFactory.getLogger(AggregatorServiceKt::class.java)
    }
}
