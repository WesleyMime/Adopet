package com.adopet.auth;

public record TokenDto(String token, Long expiresAt, String role, String username) {
}
