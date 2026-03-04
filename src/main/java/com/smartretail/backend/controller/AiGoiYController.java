package com.smartretail.backend.controller;

import com.smartretail.backend.entity.AiGoiY;
import com.smartretail.backend.service.AiGoiYService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ai-goi-y")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AiGoiYController {

    private final AiGoiYService aiGoiYService;

    @GetMapping
    public ResponseEntity<List<AiGoiY>> getAllAiGoiY() {
        List<AiGoiY> goiYs = aiGoiYService.getAllAiGoiY();
        return ResponseEntity.ok(goiYs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiGoiY> getAiGoiYById(@PathVariable Integer id) {
        Optional<AiGoiY> goiY = aiGoiYService.getAiGoiYById(id);
        return goiY.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<AiGoiY>> getAiGoiYByAgent(@PathVariable Integer agentId) {
        List<AiGoiY> goiYs = aiGoiYService.getAiGoiYByAgent(agentId);
        return ResponseEntity.ok(goiYs);
    }

    @GetMapping("/khach-hang/{khachHangId}")
    public ResponseEntity<List<AiGoiY>> getAiGoiYByKhachHang(@PathVariable Integer khachHangId) {
        List<AiGoiY> goiYs = aiGoiYService.getAiGoiYByKhachHang(khachHangId);
        return ResponseEntity.ok(goiYs);
    }

    @GetMapping("/khach-hang/{khachHangId}/top-recommendations")
    public ResponseEntity<List<AiGoiY>> getTopRecommendationsByCustomer(@PathVariable Integer khachHangId) {
        List<AiGoiY> goiYs = aiGoiYService.getRecommendationsByCustomerOrderByScore(khachHangId);
        return ResponseEntity.ok(goiYs);
    }

    @GetMapping("/bien-the/{bienTheId}")
    public ResponseEntity<List<AiGoiY>> getAiGoiYByBienThe(@PathVariable Integer bienTheId) {
        List<AiGoiY> goiYs = aiGoiYService.getAiGoiYByBienThe(bienTheId);
        return ResponseEntity.ok(goiYs);
    }

    @GetMapping("/agent/{agentId}/khach-hang/{khachHangId}/top")
    public ResponseEntity<List<AiGoiY>> getTopRecommendationsByAgentAndCustomer(
            @PathVariable Integer agentId,
            @PathVariable Integer khachHangId) {
        List<AiGoiY> goiYs = aiGoiYService.getTopRecommendationsByAgentAndCustomer(agentId, khachHangId);
        return ResponseEntity.ok(goiYs);
    }

    @GetMapping("/bien-the/{bienTheId}/average-score")
    public ResponseEntity<Double> getAverageScoreForProductVariant(@PathVariable Integer bienTheId) {
        Double averageScore = aiGoiYService.getAverageScoreForProductVariant(bienTheId);
        return ResponseEntity.ok(averageScore);
    }

    @PostMapping
    public ResponseEntity<AiGoiY> createAiGoiY(@RequestBody AiGoiY aiGoiY) {
        AiGoiY savedGoiY = aiGoiYService.saveAiGoiY(aiGoiY);
        return ResponseEntity.ok(savedGoiY);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AiGoiY> updateAiGoiY(@PathVariable Integer id, @RequestBody AiGoiY aiGoiY) {
        aiGoiY.setGoiYId(id);
        AiGoiY updatedGoiY = aiGoiYService.updateAiGoiY(aiGoiY);
        return ResponseEntity.ok(updatedGoiY);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAiGoiY(@PathVariable Integer id) {
        aiGoiYService.deleteAiGoiY(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/agent/{agentId}")
    public ResponseEntity<Void> deleteByAgentId(@PathVariable Integer agentId) {
        aiGoiYService.deleteByAgentId(agentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/khach-hang/{khachHangId}")
    public ResponseEntity<Void> deleteByKhachHangId(@PathVariable Integer khachHangId) {
        aiGoiYService.deleteByKhachHangId(khachHangId);
        return ResponseEntity.noContent().build();
    }
}