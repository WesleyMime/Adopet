package com.adopet.abrigo;

import com.adopet.user.Roles;
import com.adopet.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "Abrigo")
@Getter
@Setter
public class AbrigoEntity extends UserEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = false, length = 50)
    private String location;

    public AbrigoEntity() {
        this.setAuthorities(List.of(Roles.ABRIGO));
    }

    public AbrigoEntity(String email, String password, String name, String phone, String location) {
        super(email, password, List.of(Roles.ABRIGO));
        this.name = name;
        this.phone = phone;
        this.location = location;
    }
}
