package com.dpranantha.coroutineinterops.controller.exception;

public class ProductNotFoundException extends Throwable {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
