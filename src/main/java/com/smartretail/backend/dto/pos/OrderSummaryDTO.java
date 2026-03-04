package com.smartretail.backend.dto.pos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {

    private Integer id;
    private String orderNumber;

    private Integer storeId;
    private Integer staffId;

    private String customerName;
    private String customerPhone;

    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;

    private String paymentMethod;   // CASH / QR_CODE / CARD
    private String status;          // PENDING / COMPLETED / ...

    private OffsetDateTime createdAt;
}

