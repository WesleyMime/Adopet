package com.adopet.pet;

import com.adopet.CrudController;
import com.adopet.pet.dto.PetDto;
import com.adopet.pet.dto.PetForm;
import com.adopet.pet.dto.PetPatchForm;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pets")
@AllArgsConstructor
public class PetController implements CrudController<PetDto, PetForm, PetPatchForm> {

    private final PetService service;

    public ResponseEntity<List<PetDto>> getAll() {
        List<PetDto> all = service.getAllPets();
        if (all.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/adopt")
    public ResponseEntity<List<PetDto>> getNotAdopted() {
        List<PetDto> list = service.getNotAdopted();
        if (list.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<PetDto> getById(UUID id) {
        PetDto petById = service.getPetById(id);
        return ResponseEntity.ofNullable(petById);
    }

    public ResponseEntity<PetDto> insertNew(PetForm PetForm) {
        PetDto pet = service.insertNewPet(PetForm);
        return ResponseEntity.created(URI.create("/pets/" + pet.id().toString())).body(pet);
    }

    public ResponseEntity<PetDto> update(UUID id, PetForm PetForm) {
        PetDto updatedPet= service.updatePet(id, PetForm);
        return ResponseEntity.ofNullable(updatedPet);
    }

    public ResponseEntity<PetDto> patch(UUID id, PetPatchForm AbrigoForm) {
        PetDto patchedPet= service.patchPet(id, AbrigoForm);
        return ResponseEntity.ofNullable(patchedPet);
    }

    public ResponseEntity<PetDto> delete(UUID id) {
        PetDto pet = service.deletePet(id);
        return ResponseEntity.ofNullable(pet);
    }
}
