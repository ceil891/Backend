package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "cua_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuaHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cua_hang_id")
    private Integer cuaHangId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khu_vuc_id", nullable = false)
    private KhuVuc khuVuc;

    @Column(name = "ten_cua_hang", length = 200, nullable = false)
    private String tenCuaHang;

    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "dien_thoai", length = 20)
    private String dienThoai;

    @Column(name = "gio_mo_cua")
    private Time gioMoCua;

    @Column(name = "gio_dong_cua")
    private Time gioDongCua;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @OneToMany(mappedBy = "cuaHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NhanVien> nhanViens;

    @OneToMany(mappedBy = "cuaHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "cuaHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhieuNhapHang> phieuNhapHangs;

    @OneToMany(mappedBy = "cuaHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AiDuDoan> aiDuDoans;
}