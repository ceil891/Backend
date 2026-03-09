package com.smartretail.backend.controller;

import com.smartretail.backend.entity.NhaCungCap;
import com.smartretail.backend.service.NhaCungCapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/nha-cung-cap")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NhaCungCapController {

    private final NhaCungCapService nhaCungCapService;

    @GetMapping
    public ResponseEntity<List<NhaCungCap>> getAll(
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(nhaCungCapService.getAll(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NhaCungCap> getById(@PathVariable Integer id) {
        return nhaCungCapService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NhaCungCap> create(@RequestBody NhaCungCap body) {
        NhaCungCap saved = nhaCungCapService.create(body);
        return ResponseEntity
                .created(URI.create("/api/v1/nha-cung-cap/" + saved.getNhaCungCapId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NhaCungCap> update(
            @PathVariable Integer id,
            @RequestBody NhaCungCap body
    ) {
        NhaCungCap saved = nhaCungCapService.update(id, body);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        nhaCungCapService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

