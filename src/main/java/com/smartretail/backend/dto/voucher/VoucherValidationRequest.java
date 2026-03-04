package com.smartretail.backend.dto.voucher;

import lombok.Data;

@Data
public class VoucherValidationRequest {
    private String code;           // Mã voucher
    private Double orderAmount;    // Tổng tiền đơn hàng
    private Integer userId;        // ID user (optional)
}
