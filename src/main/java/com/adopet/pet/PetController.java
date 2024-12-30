package com.adopet.pet;

import com.adopet.CrudController;
import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.pet.dto.PetDto;
import com.adopet.pet.dto.PetForm;
import com.adopet.pet.dto.PetPatchForm;
import com.adopet.pet.dto.PetWithoutAbrigoDto;
import lombok.AllArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/pets")
@AllArgsConstructor
public class PetController implements CrudController<PetDto, PetForm, PetPatchForm> {

    private final PetService service;

    public ResponseEntity<PagedModel<PetDto>> getAll(Integer page) {
        page = page == null ? 0 : page;
        PagedModel<PetDto> petPagedModel = service.getAllPets(page);
        if (petPagedModel.getContent().isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(petPagedModel);
    }

    @PostMapping("/abrigo")
    public ResponseEntity<PagedModel<PetWithoutAbrigoDto>> getAllByAbrigo(@RequestBody AbrigoForm abrigoForm, Integer page) {
        page = page == null ? 0 : page;
        PagedModel<PetWithoutAbrigoDto> petPagedModel = service.getAllPetsByAbrigo(abrigoForm.email(), page);
        if (petPagedModel.getContent().isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(petPagedModel);
    }

    @GetMapping("/adopt")
    public ResponseEntity<PagedModel<PetDto>> getNotAdopted(@RequestParam(name = "page", required = false) Integer page) {
        page = page == null ? 0 : page;
        PagedModel<PetDto> petPagedModel = service.getNotAdopted(page);
        if (petPagedModel.getContent().isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(petPagedModel);
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
