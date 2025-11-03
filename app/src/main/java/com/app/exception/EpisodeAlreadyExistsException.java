package com.app.exception;

public class EpisodeAlreadyExistsException extends RuntimeException {
    public EpisodeAlreadyExistsException(String message) {
        super("Episode with number: " + message + " already exists");
    }
}
