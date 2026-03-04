package com.smartretail.backend.controller;

import com.smartretail.backend.entity.AiDuDoan;
import com.smartretail.backend.service.AiDuDoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ai-du-doan")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AiDuDoanController {

    private final AiDuDoanService aiDuDoanService;

    @GetMapping
    public ResponseEntity<List<AiDuDoan>> getAllAiDuDoan() {
        List<AiDuDoan> duDoans = aiDuDoanService.getAllAiDuDoan();
        return ResponseEntity.ok(duDoans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiDuDoan> getAiDuDoanById(@PathVariable Integer id) {
        Optional<AiDuDoan> duDoan = aiDuDoanService.getAiDuDoanById(id);
        return duDoan.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<AiDuDoan>> getAiDuDoanByAgent(@PathVariable Integer agentId) {
        List<AiDuDoan> duDoans = aiDuDoanService.getAiDuDoanByAgent(agentId);
        return ResponseEntity.ok(duDoans);
    }

    @GetMapping("/cua-hang/{cuaHangId}")
    public ResponseEntity<List<AiDuDoan>> getAiDuDoanByCuaHang(@PathVariable Integer cuaHangId) {
        List<AiDuDoan> duDoans = aiDuDoanService.getAiDuDoanByCuaHang(cuaHangId);
        return ResponseEntity.ok(duDoans);
    }

    @GetMapping("/bien-the/{bienTheId}")
    public ResponseEntity<List<AiDuDoan>> getAiDuDoanByBienThe(@PathVariable Integer bienTheId) {
        List<AiDuDoan> duDoans = aiDuDoanService.getAiDuDoanByBienThe(bienTheId);
        return ResponseEntity.ok(duDoans);
    }

    @GetMapping("/loai/{loaiDuDoan}")
    public ResponseEntity<List<AiDuDoan>> getAiDuDoanByLoaiDuDoan(@PathVariable String loaiDuDoan) {
        List<AiDuDoan> duDoans = aiDuDoanService.getAiDuDoanByLoaiDuDoan(loaiDuDoan);
        return ResponseEntity.ok(duDoans);
    }

    @GetMapping("/cua-hang/{cuaHangId}/loai/{loaiDuDoan}")
    public ResponseEntity<List<AiDuDoan>> getDuDoanByCuaHangAndLoai(
            @PathVariable Integer cuaHangId,
            @PathVariable String loaiDuDoan) {
        List<AiDuDoan> duDoans = aiDuDoanService.getDuDoanByCuaHangAndLoai(cuaHangId, loaiDuDoan);
        return ResponseEntity.ok(duDoans);
    }

    @GetMapping("/bien-the/{bienTheId}/period/{khoangThoiGian}")
    public ResponseEntity<List<AiDuDoan>> getDuDoanByBienTheAndPeriod(
            @PathVariable Integer bienTheId,
            @PathVariable String khoangThoiGian) {
        List<AiDuDoan> duDoans = aiDuDoanService.getDuDoanByBienTheAndPeriod(bienTheId, khoangThoiGian);
        return ResponseEntity.ok(duDoans);
    }

    @GetMapping("/cua-hang/{cuaHangId}/sales-prediction")
    public ResponseEntity<Double> getTotalPredictedSalesForStore(
            @PathVariable Integer cuaHangId,
            @RequestParam String period) {
        Double totalSales = aiDuDoanService.getTotalPredictedSalesForStore(cuaHangId, period);
        return ResponseEntity.ok(totalSales);
    }

    @PostMapping
    public ResponseEntity<AiDuDoan> createAiDuDoan(@RequestBody AiDuDoan aiDuDoan) {
        AiDuDoan savedDuDoan = aiDuDoanService.saveAiDuDoan(aiDuDoan);
        return ResponseEntity.ok(savedDuDoan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AiDuDoan> updateAiDuDoan(@PathVariable Integer id, @RequestBody AiDuDoan aiDuDoan) {
        aiDuDoan.setDuDoanId(id);
        AiDuDoan updatedDuDoan = aiDuDoanService.updateAiDuDoan(aiDuDoan);
        return ResponseEntity.ok(updatedDuDoan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAiDuDoan(@PathVariable Integer id) {
        aiDuDoanService.deleteAiDuDoan(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/agent/{agentId}")
    public ResponseEntity<Void> deleteByAgentId(@PathVariable Integer agentId) {
        aiDuDoanService.deleteByAgentId(agentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cua-hang/{cuaHangId}")
    public ResponseEntity<Void> deleteByCuaHangId(@PathVariable Integer cuaHangId) {
        aiDuDoanService.deleteByCuaHangId(cuaHangId);
        return ResponseEntity.noContent().build();
    }
}