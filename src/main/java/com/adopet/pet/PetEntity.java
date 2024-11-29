package com.adopet.pet;

import com.adopet.abrigo.AbrigoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "pet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private AbrigoEntity abrigo;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 20)
    private String size;

    @Column(nullable = false)
    private String description;

    private Boolean adopted = false;

    @Column(nullable = false, length = 20)
    private String age;

    @Column(nullable = false, length = 50)
    private String address;

    @Column(nullable = false)
    private String image;

    public PetEntity(AbrigoEntity abrigo, String name, String size, String description, String age, String address, String image) {
        this.abrigo = abrigo;
        this.name = name;
        this.size = size;
        this.description = description;
        this.adopted = false;
        this.age = age;
        this.address = address;
        this.image = image;
    }
}
