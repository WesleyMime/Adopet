package com.adopet.abrigo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AbrigoRepository extends JpaRepository<AbrigoEntity, UUID> {
}
