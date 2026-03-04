package com.smartretail.backend.dto.ai;

import lombok.Data;
import java.util.Map;

@Data
public class N8nWebhookRequest {
    private String eventType;         // LOW_STOCK, DEMAND_PREDICTION, etc.
    private Integer storeId;
    private Integer productId;
    private Map<String, Object> data; // Dữ liệu từ n8n workflow
}
