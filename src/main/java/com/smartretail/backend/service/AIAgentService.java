package com.smartretail.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartretail.backend.dto.ai.AIRecommendationResponse;
import com.smartretail.backend.dto.ai.DemandPredictionResponse;
import com.smartretail.backend.dto.ai.N8nWebhookRequest;
import com.smartretail.backend.entity.AIRecommendation;
import com.smartretail.backend.repository.AIRecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AIAgentService {

    private final AIRecommendationRepository aiRecommendationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${n8n.webhook.url:http://localhost:5678/webhook}")
    private String n8nWebhookUrl;

    public List<AIRecommendationResponse> getRecommendations(
            Integer storeId, String type, String priority, Boolean isResolved) {
        List<AIRecommendation> recommendations = aiRecommendationRepository.findByFilters(
                storeId, type, priority, isResolved);
        return recommendations.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<DemandPredictionResponse> getPredictions(Integer storeId) {
        // Gọi n8n webhook để lấy predictions
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("storeId", storeId);
            requestData.put("action", "GET_PREDICTIONS");
            requestData.put("timestamp", System.currentTimeMillis());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestData, headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) 
                    (ResponseEntity<?>) restTemplate.postForEntity(
                            n8nWebhookUrl + "/predictions", request, Map.class);
            
            if (response.getBody() != null) {
                log.info("Received predictions from n8n: {}", response.getBody());
                // Parse response từ n8n
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> predictionsData = (List<Map<String, Object>>) response.getBody().get("predictions");
                if (predictionsData != null) {
                    return predictionsData.stream()
                            .map(this::parsePrediction)
                            .collect(Collectors.toList());
                }
            }
            return getMockPredictions(storeId);
        } catch (Exception e) {
            log.error("Error calling n8n webhook for predictions", e);
            // Fallback: trả về mock data nếu n8n không available
            return getMockPredictions(storeId);
        }
    }

    private DemandPredictionResponse parsePrediction(Map<String, Object> data) {
        return DemandPredictionResponse.builder()
                .productId((Integer) data.get("productId"))
                .productName((String) data.get("productName"))
                .currentStock(((Number) data.get("currentStock")).intValue())
                .predictedDemand(((Number) data.get("predictedDemand")).intValue())
                .recommendedOrder(((Number) data.get("recommendedOrder")).intValue())
                .confidence(((Number) data.get("confidence")).doubleValue())
                .period((String) data.get("period"))
                .build();
    }

    private List<DemandPredictionResponse> getMockPredictions(Integer storeId) {
        // Mock data khi n8n không available
        return List.of(
                DemandPredictionResponse.builder()
                        .productId(1)
                        .productName("Coca Cola 330ml")
                        .currentStock(150)
                        .predictedDemand(180)
                        .recommendedOrder(200)
                        .confidence(85.0)
                        .period("7 ngày")
                        .build()
        );
    }

    @Transactional
    public AIRecommendationResponse createRecommendationFromN8n(N8nWebhookRequest request) {
        AIRecommendation recommendation = new AIRecommendation();
        recommendation.setType(request.getEventType());
        recommendation.setStoreId(request.getStoreId());
        recommendation.setProductId(request.getProductId());
        recommendation.setTitle(generateTitle(request.getEventType()));
        recommendation.setMessage(generateMessage(request.getEventType(), request.getData()));
        recommendation.setPriority(determinePriority(request.getEventType()));
        recommendation.setIsRead(false);
        recommendation.setIsResolved(false);
        recommendation.setCreatedAt(LocalDateTime.now());

        // Lưu data dưới dạng JSON string
        try {
            recommendation.setDataJson(objectMapper.writeValueAsString(request.getData()));
        } catch (JsonProcessingException e) {
            log.error("Error serializing data to JSON", e);
            recommendation.setDataJson("{}");
        }

        AIRecommendation saved = aiRecommendationRepository.save(recommendation);
        return toResponse(saved);
    }

    @Transactional
    public AIRecommendationResponse markAsRead(Integer id) {
        AIRecommendation recommendation = aiRecommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation không tồn tại"));
        recommendation.setIsRead(true);
        AIRecommendation saved = aiRecommendationRepository.save(recommendation);
        return toResponse(saved);
    }

    @Transactional
    public AIRecommendationResponse markAsResolved(Integer id) {
        AIRecommendation recommendation = aiRecommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation không tồn tại"));
        recommendation.setIsResolved(true);
        recommendation.setResolvedAt(LocalDateTime.now());
        AIRecommendation saved = aiRecommendationRepository.save(recommendation);
        return toResponse(saved);
    }

    public void triggerN8nWorkflow(String workflowName, Map<String, Object> data) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("workflow", workflowName);
            requestData.put("data", data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestData, headers);

            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) 
                    (ResponseEntity<?>) restTemplate.postForEntity(
                            n8nWebhookUrl + "/trigger", request, Map.class);
            if (response.getBody() != null) {
                log.info("Workflow trigger response: {}", response.getBody());
            }
            log.info("Triggered n8n workflow: {}", workflowName);
        } catch (Exception e) {
            log.error("Error triggering n8n workflow: {}", workflowName, e);
        }
    }

    private String generateTitle(String eventType) {
        switch (eventType) {
            case "LOW_STOCK":
                return "Cảnh Báo Tồn Kho Thấp";
            case "DEMAND_PREDICTION":
                return "Dự Đoán Nhu Cầu Tăng";
            case "SLOW_MOVING":
                return "Sản Phẩm Bán Chậm";
            case "REVENUE_ANOMALY":
                return "Doanh Thu Bất Thường";
            case "TRANSFER_SUGGESTION":
                return "Đề Xuất Điều Chuyển Kho";
            default:
                return "Đề Xuất Từ AI";
        }
    }

    private String generateMessage(String eventType, Map<String, Object> data) {
        // Generate message từ data
        return "Đề xuất từ AI Agent: " + eventType;
    }

    private String determinePriority(String eventType) {
        switch (eventType) {
            case "LOW_STOCK":
            case "REVENUE_ANOMALY":
                return "HIGH";
            case "DEMAND_PREDICTION":
                return "MEDIUM";
            case "SLOW_MOVING":
            case "TRANSFER_SUGGESTION":
                return "LOW";
            default:
                return "MEDIUM";
        }
    }

    private AIRecommendationResponse toResponse(AIRecommendation recommendation) {
        Map<String, Object> data = new HashMap<>();
        try {
            if (recommendation.getDataJson() != null && !recommendation.getDataJson().isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> parsedData = objectMapper.readValue(recommendation.getDataJson(), Map.class);
                data = parsedData;
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing data JSON", e);
        }

        return AIRecommendationResponse.builder()
                .id(recommendation.getRecommendationId())
                .type(recommendation.getType())
                .storeId(recommendation.getStoreId())
                .productId(recommendation.getProductId())
                .title(recommendation.getTitle())
                .message(recommendation.getMessage())
                .priority(recommendation.getPriority())
                .data(data)
                .isRead(recommendation.getIsRead())
                .isResolved(recommendation.getIsResolved())
                .createdAt(recommendation.getCreatedAt())
                .resolvedAt(recommendation.getResolvedAt())
                .build();
    }
}
