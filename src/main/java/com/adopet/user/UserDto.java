package com.adopet.user;

import java.util.List;

public record UserDto(String email, String password, List<Roles> authorities) {
}
