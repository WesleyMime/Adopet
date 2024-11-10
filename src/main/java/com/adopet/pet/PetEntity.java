package com.adopet.pet;

import com.adopet.abrigo.AbrigoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "pet")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private AbrigoEntity abrigo;

    @Column(nullable = false, length = 30)
    private String name;

    private String description;

    private Boolean adopted;

    @Column(nullable = false, length = 20)
    private String age;

    @Column(nullable = false, length = 50)
    private String address;

    private String image;

    public PetEntity(AbrigoEntity abrigo, String name, String description, String age, String address, String image) {
        this.abrigo = abrigo;
        this.name = name;
        this.description = description;
        this.adopted = false;
        this.age = age;
        this.address = address;
        this.image = image;
    }

    public PetEntity update(AbrigoEntity abrigo, String name, String description, String age,
                            String address, String image) {
        if (abrigo != null) {
            this.abrigo = abrigo;
        }
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        if (age != null) {
            this.age = age;
        }
        if (address != null) {
            this.address = address;
        }
        if (image != null) {
            this.image = image;
        }
        return  this;
    }
}
