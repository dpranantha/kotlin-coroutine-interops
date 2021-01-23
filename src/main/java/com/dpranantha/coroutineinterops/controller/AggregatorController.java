package com.dpranantha.coroutineinterops.controller;

import com.dpranantha.coroutineinterops.controller.exception.ErrorMessage;
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException;
import com.dpranantha.coroutineinterops.model.ProductSummary;
import com.dpranantha.coroutineinterops.service.AggregatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/v1/products")
public class AggregatorController {
    private static final Logger logger = LoggerFactory.getLogger(AggregatorController.class);

    private final AggregatorService aggregatorService;

    @Autowired
    public AggregatorController(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @Operation(summary = "Get a product by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductSummary.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))) })
    @GetMapping("/{id}")
    public ProductSummary getProductById(@PathVariable("id") final String id) throws ProductNotFoundException {
        Instant start = Instant.now();
        final ProductSummary productSummary = aggregatorService.getProductSummary(id);
        Instant finish = Instant.now();
        logger.info("Processing time: {} ms", Duration.between(start, finish).toMillis());
        return productSummary;
    }

}
