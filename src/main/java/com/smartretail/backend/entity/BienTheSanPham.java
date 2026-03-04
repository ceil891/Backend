package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "bien_the_san_pham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BienTheSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bien_the_id")
    private Integer bienTheId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    @Column(name = "ten_bien_the", length = 100)
    private String tenBienThe;

    @Column(name = "ma_sku", length = 50, unique = true)
    private String maSku;

    @Column(name = "gia_ban", precision = 10, scale = 2)
    private BigDecimal giaBan;

    // ✅ THÊM TRƯỜNG NÀY (Vì lỗi báo thiếu setGiaNhap)
    @Column(name = "gia_nhap", precision = 10, scale = 2)
    private BigDecimal giaNhap;

    // ✅ THÊM TRƯỜNG NÀY (Vì lỗi báo thiếu setHoatDong)
    @Column(name = "hoat_dong")
    private Boolean hoatDong = true;

    @Column(name = "ma_vach", length = 50)
    private String maVach;

    @OneToMany(mappedBy = "bienThe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietHoaDon> chiTietHoaDons;

    @OneToMany(mappedBy = "bienThe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietPhieuNhap> chiTietPhieuNhaps;

    @OneToMany(mappedBy = "bienThe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AiGoiY> aiGoiYs;

    @OneToMany(mappedBy = "bienThe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AiDuDoan> aiDuDoans;
}