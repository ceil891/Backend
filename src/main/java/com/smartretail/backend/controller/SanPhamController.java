package com.smartretail.backend.controller;

import com.smartretail.backend.dto.SanPhamDTO;
import com.smartretail.backend.entity.SanPham;
import com.smartretail.backend.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/san-pham")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SanPhamController {

    private final SanPhamService sanPhamService;

    @GetMapping
    public ResponseEntity<List<SanPham>> getAllSanPham() {
        List<SanPham> sanPhams = sanPhamService.getAllSanPham();
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPham> getSanPhamById(@PathVariable Integer id) {
        Optional<SanPham> sanPham = sanPhamService.getSanPhamById(id);
        return sanPham.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<SanPham>> getActiveSanPham() {
        List<SanPham> sanPhams = sanPhamService.getActiveSanPham();
        return ResponseEntity.ok(sanPhams);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SanPham>> searchSanPham(@RequestParam String keyword) {
        List<SanPham> sanPhams = sanPhamService.searchSanPham(keyword);
        return ResponseEntity.ok(sanPhams);
    }

    // ✅ FIX LỖI: Nhận SanPhamDTO thay vì Entity để xử lý Cloudinary & Danh mục
    @PostMapping
    public ResponseEntity<SanPham> createSanPham(@RequestBody SanPhamDTO dto) {
        // Gọi hàm createSanPham bạn đã viết trong Service
        SanPham savedSanPham = sanPhamService.createSanPham(dto);
        return ResponseEntity.ok(savedSanPham);
    }

    // ✅ FIX LỖI: Cập nhật dùng DTO để đồng bộ dữ liệu
    @PutMapping("/{id}")
    public ResponseEntity<SanPham> updateSanPham(@PathVariable Integer id, @RequestBody SanPhamDTO dto) {
        SanPham updatedSanPham = sanPhamService.updateSanPham(id, dto);
        return ResponseEntity.ok(updatedSanPham);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSanPham(@PathVariable Integer id) {
        sanPhamService.deleteSanPham(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateSanPham(@PathVariable Integer id) {
        sanPhamService.deactivateSanPham(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateSanPham(@PathVariable Integer id) {
        sanPhamService.activateSanPham(id);
        return ResponseEntity.noContent().build();
    }
}