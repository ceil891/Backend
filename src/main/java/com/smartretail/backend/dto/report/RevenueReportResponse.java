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
public class RevenueReportResponse {
    private String period;              // T1, T2, ngày/tháng/năm
    private BigDecimal revenue;
    private Long orders;
    private BigDecimal averageOrderValue;
    private BigDecimal profit;
    private Double profitMargin;        // %
}
