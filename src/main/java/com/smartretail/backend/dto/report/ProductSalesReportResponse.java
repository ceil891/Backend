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
public class ProductSalesReportResponse {
    private Integer productId;
    private String productName;
    private Integer quantitySold;
    private BigDecimal revenue;
    private BigDecimal profit;
}
