package com.smartretail.backend.dto.voucher;

import lombok.Data;
import java.time.LocalDate;

@Data
public class VoucherRequest {
    private String code;              // Mã voucher
    private String name;               // Tên voucher
    private String description;        // Mô tả
    private String discountType;       // PERCENTAGE hoặc FIXED
    private Double discountValue;      // Giá trị giảm (% hoặc số tiền)
    private Double minPurchase;        // Giá trị đơn hàng tối thiểu
    private Double maxDiscount;       // Giảm giá tối đa (nếu là %)
    private LocalDate startDate;       // Ngày bắt đầu
    private LocalDate endDate;         // Ngày kết thúc
    private Integer maxUsage;          // Số lần sử dụng tối đa
    private Integer maxUsagePerUser;   // Số lần sử dụng tối đa mỗi user
    private Boolean isActive;          // Trạng thái hoạt động
}
