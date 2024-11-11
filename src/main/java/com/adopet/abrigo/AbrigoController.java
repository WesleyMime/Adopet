package com.adopet.abrigo;

import com.adopet.CrudController;
import com.adopet.abrigo.dto.AbrigoDTO;
import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.abrigo.dto.AbrigoPatchForm;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/abrigos")
@AllArgsConstructor
public class AbrigoController implements CrudController<AbrigoDTO, AbrigoForm, AbrigoPatchForm> {

    private final AbrigoService service;

    public ResponseEntity<List<AbrigoDTO>> getAll() {
        List<AbrigoDTO> all = service.getAllTutores();
        if (all.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(all);
    }

    public ResponseEntity<AbrigoDTO> getById(UUID id) {
        AbrigoDTO abrigoById = service.getTutorById(id);
        return ResponseEntity.ofNullable(abrigoById);
    }

    public ResponseEntity<AbrigoDTO> insertNew(AbrigoForm AbrigoForm) {
        AbrigoDTO abrigo = service.insertNewTutor(AbrigoForm);
        return ResponseEntity.created(URI.create("/abrigos/" + abrigo.id().toString())).body(abrigo);
    }

    public ResponseEntity<AbrigoDTO> update(UUID id, AbrigoForm AbrigoForm) {
        AbrigoDTO updatedTutor= service.updateTutor(id, AbrigoForm);
        return ResponseEntity.ofNullable(updatedTutor);
    }

    public ResponseEntity<AbrigoDTO> patch(UUID id, AbrigoPatchForm AbrigoForm) {
        AbrigoDTO patchedTutor= service.patchTutor(id, AbrigoForm);
        return ResponseEntity.ofNullable(patchedTutor);
    }

    public ResponseEntity<AbrigoDTO> delete(UUID id) {
        AbrigoDTO abrigo = service.deleteTutor(id);
        return ResponseEntity.ofNullable(abrigo);
    }
}
