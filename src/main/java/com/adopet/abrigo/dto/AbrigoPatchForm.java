package com.adopet.abrigo.dto;

import jakarta.validation.constraints.Size;

public record AbrigoPatchForm(@Size(min = 3, max = 100) String name,
                              @Size(min = 8, max = 20) String phone,
                              @Size(min = 8, max = 50) String location) {
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"name\":\"").append(name).append('\"');
        sb.append(", \"phone\":\"").append(phone).append('\"');
        sb.append(", \"location\":\"").append(location).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
