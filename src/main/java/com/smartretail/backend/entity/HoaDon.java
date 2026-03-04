package com.smartretail.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference; // ✅ Bắt buộc import
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "hoa_don")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hoa_don_id")
    private Integer hoaDonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cua_hang_id", nullable = false)
    private CuaHang cuaHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nhan_vien_id", nullable = false)
    private NhanVien nhanVien;

    @Column(name = "ngay_lap", nullable = false)
    private Timestamp ngayLap;

    @Column(name = "tam_tinh", precision = 10, scale = 2)
    private BigDecimal tamTinh;

    @Column(name = "tien_thue", precision = 10, scale = 2)
    private BigDecimal tienThue;

    @Column(name = "chiet_khau", precision = 10, scale = 2)
    private BigDecimal chietKhau;

    @Column(name = "phi_ship", precision = 10, scale = 2)
    private BigDecimal phiShip;

    @Column(name = "tong_phai_thanh_toan", precision = 10, scale = 2)
    private BigDecimal tongPhaiThanhToan;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    // ✅ VŨ KHÍ BÍ MẬT: Ép Spring Boot gửi danh sách này cho React
    @JsonManagedReference
    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // Đổi LAZY thành EAGER
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ChiTietHoaDon> chiTietHoaDons;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiaoDichThanhToan> giaoDichThanhToans;

    @OneToOne(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private GiaoHang giaoHang;
}