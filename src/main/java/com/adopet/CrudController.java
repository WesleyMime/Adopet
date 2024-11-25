package com.adopet;

import jakarta.validation.Valid;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface CrudController<DTO, Form, PatchForm> {

    @GetMapping
    ResponseEntity<PagedModel<DTO>> getAll(@RequestParam(name = "page", required = false) Integer page);

    @GetMapping("/{id}")
    ResponseEntity<DTO> getById(@PathVariable(name = "id") UUID id);

    @PostMapping
    ResponseEntity<DTO> insertNew(@RequestBody @Valid Form form);

    @PutMapping("/{id}")
    ResponseEntity<DTO> update(@PathVariable(name = "id") UUID id, @RequestBody @Valid Form form);

    @PatchMapping("/{id}")
    ResponseEntity<DTO> patch(@PathVariable(name = "id") UUID id, @RequestBody @Valid PatchForm form);

    @DeleteMapping("/{id}")
    ResponseEntity<DTO> delete(@PathVariable(name = "id") UUID id);
}
