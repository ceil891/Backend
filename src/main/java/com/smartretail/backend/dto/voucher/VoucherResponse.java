package com.smartretail.backend.dto.voucher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {
    private Integer id;
    private String code;
    private String name;
    private String description;
    private String discountType;       // PERCENTAGE hoặc FIXED
    private Double discountValue;
    private Double minPurchase;
    private Double maxDiscount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxUsage;
    private Integer maxUsagePerUser;
    private Integer currentUsage;      // Số lần đã sử dụng
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
