package com.smartretail.backend.dto.pos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOrderRequest {

    private Integer cuaHangId;          // cửa hàng hiện tại (tạm thời bắt buộc)
    private Integer nhanVienId;         // nhân viên thực hiện giao dịch

    private String customerName;        // tên khách lẻ
    private String customerPhone;       // SĐT khách lẻ

    private BigDecimal discount;        // tổng giảm giá trên đơn (nếu có)
    private BigDecimal shippingFee;     // phí ship (nếu có)

    private String paymentMethod;       // CASH / QR_CODE / CARD

    private String maHoaDon;            // tùy chọn (nếu frontend muốn set mã)
    private String kenhBan;             // RETAIL / ONLINE / SHOPEE / FACEBOOK / WEBSITE ...
    private String status;              // tùy chọn
    private String notes;               // ghi chú

    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Integer bienTheId;      // id biến thể sản phẩm (bắt buộc)
        private Integer quantity;       // số lượng
        private BigDecimal unitPrice;   // đơn giá tại thời điểm bán
        private BigDecimal discount;    // giảm giá cho item (nếu có)
    }
}

