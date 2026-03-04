package com.smartretail.backend.service;

import com.smartretail.backend.entity.AiDuDoan;
import com.smartretail.backend.repository.AiDuDoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AiDuDoanService {

    private final AiDuDoanRepository aiDuDoanRepository;

    public List<AiDuDoan> getAllAiDuDoan() {
        return aiDuDoanRepository.findAll();
    }

    public Optional<AiDuDoan> getAiDuDoanById(Integer id) {
        return aiDuDoanRepository.findById(id);
    }

    public List<AiDuDoan> getAiDuDoanByAgent(Integer agentId) {
        return aiDuDoanRepository.findByAgentId(agentId);
    }

    public List<AiDuDoan> getAiDuDoanByCuaHang(Integer cuaHangId) {
        return aiDuDoanRepository.findByCuaHangId(cuaHangId);
    }

    public List<AiDuDoan> getAiDuDoanByBienThe(Integer bienTheId) {
        return aiDuDoanRepository.findByBienTheId(bienTheId);
    }

    public List<AiDuDoan> getAiDuDoanByLoaiDuDoan(String loaiDuDoan) {
        return aiDuDoanRepository.findByLoaiDuDoan(loaiDuDoan);
    }

    public List<AiDuDoan> getDuDoanByCuaHangAndLoai(Integer cuaHangId, String loaiDuDoan) {
        return aiDuDoanRepository.findByCuaHangAndLoaiDuDoanOrderByGiaTriDesc(cuaHangId, loaiDuDoan);
    }

    public List<AiDuDoan> getDuDoanByBienTheAndPeriod(Integer bienTheId, String khoangThoiGian) {
        return aiDuDoanRepository.findByBienTheAndKhoangThoiGian(bienTheId, khoangThoiGian);
    }

    public Double getTotalPredictedSalesForStore(Integer cuaHangId, String period) {
        return aiDuDoanRepository.getTotalPredictedSalesForStore(cuaHangId, period);
    }

    public AiDuDoan saveAiDuDoan(AiDuDoan aiDuDoan) {
        return aiDuDoanRepository.save(aiDuDoan);
    }

    public AiDuDoan updateAiDuDoan(AiDuDoan aiDuDoan) {
        return aiDuDoanRepository.save(aiDuDoan);
    }

    public void deleteAiDuDoan(Integer id) {
        aiDuDoanRepository.deleteById(id);
    }

    public void deleteByAgentId(Integer agentId) {
        List<AiDuDoan> duDoans = aiDuDoanRepository.findByAgentId(agentId);
        aiDuDoanRepository.deleteAll(duDoans);
    }

    public void deleteByCuaHangId(Integer cuaHangId) {
        List<AiDuDoan> duDoans = aiDuDoanRepository.findByCuaHangId(cuaHangId);
        aiDuDoanRepository.deleteAll(duDoans);
    }
}