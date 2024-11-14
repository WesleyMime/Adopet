package com.adopet.adocao;

import com.adopet.adocao.dto.AdocaoDto;
import com.adopet.adocao.dto.AdocaoForm;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/adocao")
@AllArgsConstructor
public class AdocaoController {

    private AdocaoService service;

    @PostMapping
    public ResponseEntity<AdocaoDto> insertNew(@RequestBody @Valid AdocaoForm adocaoForm) {
        AdocaoDto adocaoDto = service.insertNew(adocaoForm);
        return ResponseEntity.created(URI.create(adocaoDto.id().toString())).body(adocaoDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AdocaoDto> delete(@PathVariable("id") UUID id) {
        AdocaoDto adocaoDto = service.deleteAdocao(id);
        return ResponseEntity.ofNullable(adocaoDto);
    }
}
