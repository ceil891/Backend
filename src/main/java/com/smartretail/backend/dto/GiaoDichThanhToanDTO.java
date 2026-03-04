package com.smartretail.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiaoDichThanhToanDTO {

    private Integer giaoDichId;
    private Integer hoaDonId;
    private Integer hinhThucId;
    private String tenHinhThuc;
    private BigDecimal soTien;
    private Timestamp thoiGian;
    private String trangThai;
}