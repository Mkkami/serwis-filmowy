package com.app.entity.dto;

public record UserLoginRequest(
        String username,
        String password
) {}
