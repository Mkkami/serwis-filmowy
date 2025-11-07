package com.app.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(Long message) {
        super("Film with id: " + message + " not found");
    }
}
