package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "phieu_nhap_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhieuNhapHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phieu_nhap_id")
    private Integer phieuNhapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nha_cung_cap_id", nullable = false)
    private NhaCungCap nhaCungCap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cua_hang_id", nullable = false)
    private CuaHang cuaHang;

    @Column(name = "ngay_nhap", nullable = false)
    private Timestamp ngayNhap;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietPhieuNhap> chiTietPhieuNhaps;
}