package com.smartretail.backend.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalProducts;
    private Long lowStockProducts;
    private Long pendingRecommendations;
    private Double revenueGrowth;  // % tăng trưởng
    private Double orderGrowth;    // % tăng trưởng
}
