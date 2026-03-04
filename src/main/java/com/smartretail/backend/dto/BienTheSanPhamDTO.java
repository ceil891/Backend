package com.smartretail.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienTheSanPhamDTO {

    private Integer bienTheId;
    private Integer sanPhamId;
    private String tenBienThe;
    private String maSku;
    private BigDecimal giaBan;
    private String maVach;
    private Integer soLuongTon; // Có thể tính từ inventory hoặc phiếu nhập/xuất
}