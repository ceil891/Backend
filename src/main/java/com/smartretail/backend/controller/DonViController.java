package com.smartretail.backend.controller;

import com.smartretail.backend.entity.DonVi;
import com.smartretail.backend.service.DonViService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/don-vi")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DonViController {

    private final DonViService donViService;

    @GetMapping
    public ResponseEntity<List<DonVi>> getAllDonVi() {
        List<DonVi> donVis = donViService.getAllDonVi();
        return ResponseEntity.ok(donVis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonVi> getDonViById(@PathVariable Integer id) {
        Optional<DonVi> donVi = donViService.getDonViById(id);
        return donVi.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DonVi> createDonVi(@RequestBody DonVi donVi) {
        DonVi savedDonVi = donViService.saveDonVi(donVi);
        return ResponseEntity.ok(savedDonVi);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonVi> updateDonVi(@PathVariable Integer id, @RequestBody DonVi donVi) {
        donVi.setDonViId(id);
        DonVi updatedDonVi = donViService.updateDonVi(donVi);
        return ResponseEntity.ok(updatedDonVi);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonVi(@PathVariable Integer id) {
        donViService.deleteDonVi(id);
        return ResponseEntity.noContent().build();
    }
}
