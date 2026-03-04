package com.smartretail.backend.controller;

import com.smartretail.backend.entity.KhuyenMai;
import com.smartretail.backend.repository.KhuyenMaiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/khuyen-mai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KhuyenMaiController {

    private final KhuyenMaiRepository khuyenMaiRepository;

    @GetMapping
    public ResponseEntity<List<KhuyenMai>> getAll() {
        return ResponseEntity.ok(khuyenMaiRepository.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<KhuyenMai>> getActive() {
        LocalDate today = LocalDate.now();
        return ResponseEntity.ok(
                khuyenMaiRepository.findByActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
        );
    }

    @PostMapping
    public ResponseEntity<KhuyenMai> create(@RequestBody KhuyenMai body) {
        body.setId(null);
        KhuyenMai saved = khuyenMaiRepository.save(body);
        return ResponseEntity.created(URI.create("/api/v1/khuyen-mai/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KhuyenMai> update(@PathVariable Integer id, @RequestBody KhuyenMai body) {
        return khuyenMaiRepository.findById(id)
                .map(existing -> {
                    body.setId(existing.getId());
                    KhuyenMai saved = khuyenMaiRepository.save(body);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!khuyenMaiRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        khuyenMaiRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

