package com.smartretail.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamDTO {

    private Integer sanPhamId;
    private String maSku;
    private String tenSanPham;
   private Integer danhMucId;
    private String tenDanhMuc;
    private Integer donViId;
    private String tenDonVi;
    private String thuongHieu;
    private BigDecimal giaBan;
    private BigDecimal giaNhap;
    private String maVach;
    private String moTa;
    private Boolean hoatDong;
    private List<String> hinhAnhUrls;
    private List<BienTheSanPhamDTO> bienTheSanPhams;
}