package com.dpranantha.coroutineinterops.integration;

import com.dpranantha.coroutineinterops.CoroutineInteropsApplication;
import com.dpranantha.coroutineinterops.model.ProductOfferAndSeller;
import com.dpranantha.coroutineinterops.model.ProductSummary;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CoroutineInteropsApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = { "use.kotlin=true" } )
class CoroutineInteropsIT {

    //Note: execution via mvn clean verify -Pintegration has some overhead
    @Test
    void testExistsProduct_thenReceived_200_Response() {
        final ProductSummary productSummary = when().request("GET", "/v1/products/1")
                .then()
                .time(greaterThan(1200L), TimeUnit.MILLISECONDS)  //change 6 call x 200ms (4 concurrent) + with overhead >= 300ms
                .time(lessThan(1500L), TimeUnit.MILLISECONDS)
                .assertThat()
                .statusCode(200)
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

    //Note: execution via mvn clean verify -Pintegration has some overhead
    @Test
    void testNotExistsProduct_thenReceived_404_Response() {
        when().request("GET", "/v1/products/1100")
                .then()
                .time(greaterThan(200L), TimeUnit.MILLISECONDS)
                .time(lessThan(300L), TimeUnit.MILLISECONDS)
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Product can't be found!"))
                .body("errorCode", equalTo(404));
    }
}
