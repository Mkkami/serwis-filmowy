package com.app.exception;

public class SeriesNotFoundException extends RuntimeException {
    public SeriesNotFoundException(Long message) {
        super("Series with id: " + message + " not found");
    }
}
