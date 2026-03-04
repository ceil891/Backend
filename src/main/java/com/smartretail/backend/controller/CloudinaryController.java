package com.smartretail.backend.controller;

import com.smartretail.backend.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cloudinary")
@RequiredArgsConstructor
@CrossOrigin("*") // Cho phép React truy cập
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    /**
     * API Upload ảnh lên Cloudinary
     * URL: POST /api/v1/cloudinary/upload?folder=products
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "general") String folder) {
        
        Map<String, Object> result = cloudinaryService.uploadImage(file, folder);
        return ResponseEntity.ok(result);
    }

    /**
     * API Xóa ảnh trên Cloudinary
     * URL: DELETE /api/v1/cloudinary/delete?publicId=smart-retail/products/abc
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteImage(@RequestParam("publicId") String publicId) {
        Map<String, Object> result = cloudinaryService.deleteImage(publicId);
        return ResponseEntity.ok(result);
    }

    /**
     * API Tiện ích: Lấy Public ID từ URL (Dùng khi cần xóa ảnh dựa trên link đã lưu)
     */
    @GetMapping("/extract-id")
    public ResponseEntity<String> getPublicId(@RequestParam("url") String url) {
        String publicId = cloudinaryService.extractPublicId(url);
        return ResponseEntity.ok(publicId);
    }
}