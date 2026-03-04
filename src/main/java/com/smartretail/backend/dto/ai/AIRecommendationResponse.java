package com.smartretail.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIRecommendationResponse {
    private Integer id;
    private String type;              // LOW_STOCK, DEMAND_PREDICTION, SLOW_MOVING, REVENUE_ANOMALY, TRANSFER_SUGGESTION
    private Integer storeId;
    private Integer productId;
    private String title;
    private String message;
    private String priority;           // LOW, MEDIUM, HIGH, URGENT
    private Map<String, Object> data; // Dữ liệu bổ sung (JSON)
    private Boolean isRead;
    private Boolean isResolved;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
