package com.dpranantha.coroutineinterops.integration;

import com.dpranantha.coroutineinterops.CoroutineInteropsApplication;
import com.dpranantha.coroutineinterops.model.ProductOfferAndSeller;
import com.dpranantha.coroutineinterops.model.ProductSummary;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CoroutineInteropsApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CoroutineInteropsIT {

    @Test
    void testExistsProduct_thenReceived_200_Response() {
        final ProductSummary productSummary = when().request("GET", "/v1/products/1")
                .then()
                .assertThat()
                .statusCode(200)
                .time(greaterThan(800L), TimeUnit.MILLISECONDS)
                .extract()
                .body()
                .as(ProductSummary.class);

        assertEquals("1", productSummary.getProductId());
        assertEquals("Product 1", productSummary.getProductName());
        assertEquals("This is product 1", productSummary.getProductDescription().get());
        assertEquals(1.0, productSummary.getProductWeightInKg().get());
        assertEquals("red", productSummary.getProductColor().get());
        assertEquals(Arrays.asList(22.99, 12.99), productSummary.getProductOfferAndSellers().stream()
                .map(ProductOfferAndSeller::getProductPrice)
                .collect(Collectors.toList()));
        assertEquals(Arrays.asList("Seller 1", "Seller 2"), productSummary.getProductOfferAndSellers().stream()
                .map(ProductOfferAndSeller::getSellerName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));
        assertEquals(4.0, productSummary.getRating());
    }

    @Test
    void testNotExistsProduct_thenReceived_404_Response() {
        when().request("GET", "/v1/products/1100")
                .then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Product can't be found!"))
                .body("errorCode", equalTo(404))
                .time(greaterThan(200L), TimeUnit.MILLISECONDS)
                .time(lessThan(300L), TimeUnit.MILLISECONDS);
    }
}
