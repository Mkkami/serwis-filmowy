package com.app.exception;

public class UnauthorizedReviewDeletionException extends RuntimeException {
    public UnauthorizedReviewDeletionException() {
        super("You are not authorized to delete this review");
    }
}
