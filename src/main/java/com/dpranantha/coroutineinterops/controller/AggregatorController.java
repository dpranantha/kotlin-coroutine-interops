package com.dpranantha.coroutineinterops.controller;

import com.dpranantha.coroutineinterops.controller.exception.ErrorMessage;
import com.dpranantha.coroutineinterops.controller.exception.ProductNotFoundException;
import com.dpranantha.coroutineinterops.model.Product;
import com.dpranantha.coroutineinterops.service.AggregatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/products")
public class AggregatorController {
    private final AggregatorService aggregatorService;

    @Autowired
    public AggregatorController(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @Operation(summary = "Get a product by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))) })
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") final String id) throws ProductNotFoundException {
        return aggregatorService.getExtendedProductInfo(id);
    }

}
