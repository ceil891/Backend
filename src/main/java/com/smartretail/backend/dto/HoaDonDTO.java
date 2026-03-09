package com.smartretail.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonDTO {

    private Integer hoaDonId;
    private String maHoaDon;
    private String kenhBan;
    private Integer cuaHangId;
    private String tenCuaHang;
    private Integer khachHangId;
    private String tenKhachHang;
    private String dienThoaiKhachHang;
    private Integer nhanVienId;
    private String tenNhanVien;
    private Timestamp ngayLap;
    private String phuongThucThanhToan;
    private String ghiChu;
    private BigDecimal tamTinh;
    private BigDecimal tienThue;
    private BigDecimal chietKhau;
    private BigDecimal phiShip;
    private BigDecimal tongPhaiThanhToan;
    private String trangThai;
    private Timestamp ngayHuy;
    private String lyDoHuy;
    private String nguoiHuy;
    private List<ChiTietHoaDonDTO> chiTietHoaDons;
    private List<GiaoDichThanhToanDTO> giaoDichThanhToans;
}