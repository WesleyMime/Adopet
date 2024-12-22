package com.adopet.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private List<Roles> authorities;

    public UserEntity(String email, String password, List<Roles> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
