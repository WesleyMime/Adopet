package com.adopet.auth;

import java.util.UUID;

public record TokenDto(String token, Long expiresAt, String role, UUID id) {
}
