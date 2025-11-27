package com.app.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long message) {
        super("Review with id: " + message + " not found");
    }
}
