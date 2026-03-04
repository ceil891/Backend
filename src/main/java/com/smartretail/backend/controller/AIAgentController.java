package com.smartretail.backend.controller;

import com.smartretail.backend.dto.ai.AIRecommendationResponse;
import com.smartretail.backend.dto.ai.DemandPredictionResponse;
import com.smartretail.backend.dto.ai.N8nWebhookRequest;
import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.service.AIAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AIAgentController {

    private final AIAgentService aiAgentService;

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<AIRecommendationResponse>>> getRecommendations(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Boolean isResolved) {
        try {
            List<AIRecommendationResponse> recommendations = aiAgentService.getRecommendations(
                    storeId, type, priority, isResolved);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách đề xuất thành công", recommendations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách đề xuất: " + e.getMessage(), null));
        }
    }

    @GetMapping("/predictions")
    public ResponseEntity<ApiResponse<List<DemandPredictionResponse>>> getPredictions(
            @RequestParam(required = false) Integer storeId) {
        try {
            List<DemandPredictionResponse> predictions = aiAgentService.getPredictions(storeId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy dự đoán thành công", predictions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy dự đoán: " + e.getMessage(), null));
        }
    }

    @PostMapping("/webhook/n8n")
    public ResponseEntity<ApiResponse<AIRecommendationResponse>> receiveN8nWebhook(
            @RequestBody N8nWebhookRequest request) {
        try {
            AIRecommendationResponse recommendation = aiAgentService.createRecommendationFromN8n(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Nhận webhook từ n8n thành công", recommendation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi xử lý webhook: " + e.getMessage(), null));
        }
    }

    @PutMapping("/recommendations/{id}/read")
    public ResponseEntity<ApiResponse<AIRecommendationResponse>> markAsRead(@PathVariable Integer id) {
        try {
            AIRecommendationResponse recommendation = aiAgentService.markAsRead(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Đánh dấu đã đọc thành công", recommendation));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi đánh dấu đã đọc: " + e.getMessage(), null));
        }
    }

    @PutMapping("/recommendations/{id}/resolve")
    public ResponseEntity<ApiResponse<AIRecommendationResponse>> markAsResolved(@PathVariable Integer id) {
        try {
            AIRecommendationResponse recommendation = aiAgentService.markAsResolved(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Đánh dấu đã xử lý thành công", recommendation));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi đánh dấu đã xử lý: " + e.getMessage(), null));
        }
    }

    @PostMapping("/trigger")
    public ResponseEntity<ApiResponse<Void>> triggerWorkflow(
            @RequestParam String workflowName,
            @RequestBody(required = false) Map<String, Object> data) {
        try {
            aiAgentService.triggerN8nWorkflow(workflowName, data != null ? data : Map.of());
            return ResponseEntity.ok(new ApiResponse<>(true, "Trigger workflow thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi trigger workflow: " + e.getMessage(), null));
        }
    }

    @PostMapping("/predict-demand")
    public ResponseEntity<ApiResponse<List<DemandPredictionResponse>>> predictDemand(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false, defaultValue = "7") Integer days) {
        try {
            // Trigger n8n workflow để tính toán dự đoán
            Map<String, Object> data = new HashMap<>();
            data.put("storeId", storeId);
            data.put("days", days);
            aiAgentService.triggerN8nWorkflow("demand-prediction", data);
            
            // Sau đó lấy kết quả
            List<DemandPredictionResponse> predictions = aiAgentService.getPredictions(storeId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Dự đoán nhu cầu thành công", predictions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi dự đoán nhu cầu: " + e.getMessage(), null));
        }
    }

    @PostMapping("/analyze-sales")
    public ResponseEntity<ApiResponse<Map<String, Object>>> analyzeSales(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false, defaultValue = "30") Integer days) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("storeId", storeId);
            data.put("productId", productId);
            data.put("days", days);
            aiAgentService.triggerN8nWorkflow("sales-analysis", data);
            
            // Trả về thông báo đã trigger
            Map<String, Object> result = new HashMap<>();
            result.put("message", "Đã trigger phân tích bán hàng, kết quả sẽ được cập nhật qua webhook");
            result.put("workflow", "sales-analysis");
            return ResponseEntity.ok(new ApiResponse<>(true, "Phân tích bán hàng đã được trigger", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi phân tích bán hàng: " + e.getMessage(), null));
        }
    }
}
