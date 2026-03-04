package com.smartretail.backend.dto.voucher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherValidationResponse {
    private Boolean isValid;
    private String message;
    private Double discountAmount;  // Số tiền được giảm
    private VoucherResponse voucher; // Thông tin voucher nếu hợp lệ
}
