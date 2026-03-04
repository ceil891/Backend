package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "nha_cung_cap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nha_cung_cap_id")
    private Integer nhaCungCapId;

    @Column(name = "ten_nha_cung_cap", length = 200, nullable = false)
    private String tenNhaCungCap;

    @Column(name = "dien_thoai", length = 20)
    private String dienThoai;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "nguoi_lien_he", length = 200)
    private String nguoiLienHe;

    @Column(name = "ma_so_thue", length = 50)
    private String maSoThue;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhieuNhapHang> phieuNhapHangs;
}