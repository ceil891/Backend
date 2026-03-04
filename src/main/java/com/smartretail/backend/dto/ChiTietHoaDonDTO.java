package com.smartretail.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDonDTO {

    private Integer chiTietId;
    private Integer hoaDonId;
    private Integer bienTheId;
    private String tenSanPham;
    private String tenBienThe;
    private String maSku;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
}