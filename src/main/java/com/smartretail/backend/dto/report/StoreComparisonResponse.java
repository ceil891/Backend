package com.smartretail.backend.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreComparisonResponse {
    private Integer storeId;
    private String storeName;
    private BigDecimal revenue;
    private Long orders;
    private BigDecimal averageOrderValue;
    private Double growth;  // % tăng trưởng
}
