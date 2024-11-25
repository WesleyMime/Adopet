package com.adopet.abrigo;

import com.adopet.CrudController;
import com.adopet.abrigo.dto.AbrigoDto;
import com.adopet.abrigo.dto.AbrigoForm;
import com.adopet.abrigo.dto.AbrigoPatchForm;
import lombok.AllArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/abrigos")
@AllArgsConstructor
public class AbrigoController implements CrudController<AbrigoDto, AbrigoForm, AbrigoPatchForm> {

    private final AbrigoService service;

    public ResponseEntity<PagedModel<AbrigoDto>> getAll(Integer page) {
        page = page == null ? 0 : page;
        PagedModel<AbrigoDto> abrigoPagedModel = service.getAllTutores(page);
        if (abrigoPagedModel.getContent().isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(abrigoPagedModel);
    }

    public ResponseEntity<AbrigoDto> getById(UUID id) {
        AbrigoDto abrigoById = service.getTutorById(id);
        return ResponseEntity.ofNullable(abrigoById);
    }

    public ResponseEntity<AbrigoDto> insertNew(AbrigoForm AbrigoForm) {
        AbrigoDto abrigo = service.insertNewTutor(AbrigoForm);
        return ResponseEntity.created(URI.create("/abrigos/" + abrigo.id().toString())).body(abrigo);
    }

    public ResponseEntity<AbrigoDto> update(UUID id, AbrigoForm AbrigoForm) {
        AbrigoDto updatedTutor= service.updateTutor(id, AbrigoForm);
        return ResponseEntity.ofNullable(updatedTutor);
    }

    public ResponseEntity<AbrigoDto> patch(UUID id, AbrigoPatchForm AbrigoForm) {
        AbrigoDto patchedTutor= service.patchTutor(id, AbrigoForm);
        return ResponseEntity.ofNullable(patchedTutor);
    }

    public ResponseEntity<AbrigoDto> delete(UUID id) {
        AbrigoDto abrigo = service.deleteTutor(id);
        return ResponseEntity.ofNullable(abrigo);
    }
}
