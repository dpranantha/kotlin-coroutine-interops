package com.dpranantha.coroutineinterops.controller.exception;

import java.io.Serializable;

public class ErrorMessage implements Serializable {
    private final String message;
    private final int errorCode;

    public ErrorMessage(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
