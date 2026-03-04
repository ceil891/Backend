package com.smartretail.backend.controller;

import com.smartretail.backend.entity.BienTheSanPham;
import com.smartretail.backend.service.BienTheSanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bien-the-san-pham")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BienTheSanPhamController {

    private final BienTheSanPhamService bienTheSanPhamService;

    @GetMapping
    public ResponseEntity<List<BienTheSanPham>> getAllBienThe() {
        List<BienTheSanPham> bienThes = bienTheSanPhamService.getAllBienThe();
        return ResponseEntity.ok(bienThes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BienTheSanPham> getBienTheById(@PathVariable Integer id) {
        Optional<BienTheSanPham> bienThe = bienTheSanPhamService.getBienTheById(id);
        return bienThe.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/san-pham/{sanPhamId}")
    public ResponseEntity<List<BienTheSanPham>> getBienTheBySanPham(@PathVariable Integer sanPhamId) {
        List<BienTheSanPham> bienThes = bienTheSanPhamService.getBienTheBySanPhamId(sanPhamId);
        return ResponseEntity.ok(bienThes);
    }

    @GetMapping("/ma-sku/{maSku}")
    public ResponseEntity<BienTheSanPham> getBienTheByMaSku(@PathVariable String maSku) {
        Optional<BienTheSanPham> bienThe = bienTheSanPhamService.getBienTheByMaSku(maSku);
        return bienThe.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ma-vach/{maVach}")
    public ResponseEntity<BienTheSanPham> getBienTheByMaVach(@PathVariable String maVach) {
        Optional<BienTheSanPham> bienThe = bienTheSanPhamService.getBienTheByMaVach(maVach);
        return bienThe.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BienTheSanPham>> searchBienThe(@RequestParam String keyword) {
        List<BienTheSanPham> bienThes = bienTheSanPhamService.searchBienThe(keyword);
        return ResponseEntity.ok(bienThes);
    }

    @GetMapping("/san-pham/{sanPhamId}/sorted-by-price")
    public ResponseEntity<List<BienTheSanPham>> getBienTheBySanPhamOrderByGiaBan(@PathVariable Integer sanPhamId) {
        List<BienTheSanPham> bienThes = bienTheSanPhamService.getBienTheBySanPhamOrderByGiaBan(sanPhamId);
        return ResponseEntity.ok(bienThes);
    }

    @PostMapping
    public ResponseEntity<BienTheSanPham> createBienThe(@RequestBody BienTheSanPham bienThe) {
        BienTheSanPham savedBienThe = bienTheSanPhamService.saveBienThe(bienThe);
        return ResponseEntity.ok(savedBienThe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BienTheSanPham> updateBienThe(@PathVariable Integer id, @RequestBody BienTheSanPham bienThe) {
        bienThe.setBienTheId(id);
        BienTheSanPham updatedBienThe = bienTheSanPhamService.updateBienThe(bienThe);
        return ResponseEntity.ok(updatedBienThe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBienThe(@PathVariable Integer id) {
        bienTheSanPhamService.deleteBienThe(id);
        return ResponseEntity.noContent().build();
    }
}