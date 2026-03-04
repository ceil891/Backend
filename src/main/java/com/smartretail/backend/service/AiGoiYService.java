package com.smartretail.backend.service;

import com.smartretail.backend.entity.AiGoiY;
import com.smartretail.backend.repository.AiGoiYRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AiGoiYService {

    private final AiGoiYRepository aiGoiYRepository;

    public List<AiGoiY> getAllAiGoiY() {
        return aiGoiYRepository.findAll();
    }

    public Optional<AiGoiY> getAiGoiYById(Integer id) {
        return aiGoiYRepository.findById(id);
    }

    public List<AiGoiY> getAiGoiYByAgent(Integer agentId) {
        return aiGoiYRepository.findByAgentId(agentId);
    }

    public List<AiGoiY> getAiGoiYByKhachHang(Integer khachHangId) {
        return aiGoiYRepository.findByKhachHangId(khachHangId);
    }

    public List<AiGoiY> getAiGoiYByBienThe(Integer bienTheId) {
        return aiGoiYRepository.findByBienTheId(bienTheId);
    }

    public List<AiGoiY> getTopRecommendationsByAgentAndCustomer(Integer agentId, Integer khachHangId) {
        return aiGoiYRepository.findTopRecommendationsByAgentAndCustomer(agentId, khachHangId);
    }

    public List<AiGoiY> getRecommendationsByCustomerOrderByScore(Integer khachHangId) {
        return aiGoiYRepository.findByKhachHangOrderByDiemSoDesc(khachHangId);
    }

    public Double getAverageScoreForProductVariant(Integer bienTheId) {
        return aiGoiYRepository.getAverageScoreForProductVariant(bienTheId);
    }

    public AiGoiY saveAiGoiY(AiGoiY aiGoiY) {
        return aiGoiYRepository.save(aiGoiY);
    }

    public AiGoiY updateAiGoiY(AiGoiY aiGoiY) {
        return aiGoiYRepository.save(aiGoiY);
    }

    public void deleteAiGoiY(Integer id) {
        aiGoiYRepository.deleteById(id);
    }

    public void deleteByAgentId(Integer agentId) {
        List<AiGoiY> goiYs = aiGoiYRepository.findByAgentId(agentId);
        aiGoiYRepository.deleteAll(goiYs);
    }

    public void deleteByKhachHangId(Integer khachHangId) {
        List<AiGoiY> goiYs = aiGoiYRepository.findByKhachHangId(khachHangId);
        aiGoiYRepository.deleteAll(goiYs);
    }
}