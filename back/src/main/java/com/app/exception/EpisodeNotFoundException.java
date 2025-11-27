package com.app.exception;

public class EpisodeNotFoundException extends RuntimeException {
    public EpisodeNotFoundException(Long message) {
        super("Episode with id: " + message + " not found");
    }
}
