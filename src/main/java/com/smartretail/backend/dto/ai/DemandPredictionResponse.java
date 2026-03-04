package com.smartretail.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandPredictionResponse {
    private Integer productId;
    private String productName;
    private Integer currentStock;
    private Integer predictedDemand;  // Nhu cầu dự đoán
    private Integer recommendedOrder;  // Số lượng đề xuất nhập
    private Double confidence;        // Độ tin cậy (0-100%)
    private String period;            // Kỳ dự đoán (7 ngày, 30 ngày...)
}
