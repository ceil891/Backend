package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "so_quy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoQuy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_quy_id")
    private Integer soQuyId;

    @Column(name = "loai", length = 10, nullable = false)
    private String loai; // "thu" hoặc "chi"

    @Column(name = "so_tien", precision = 10, scale = 2, nullable = false)
    private BigDecimal soTien;

    @Column(name = "ly_do", columnDefinition = "TEXT")
    private String lyDo;

    @Column(name = "doi_tuong", length = 150)
    private String doiTuong;

    @Column(name = "phuong_thuc", length = 50)
    private String phuongThuc;

    @Column(name = "tham_chieu", length = 50)
    private String thamChieu;

    @Column(name = "thoi_gian", nullable = false)
    private Timestamp thoiGian;

    @Column(name = "nhan_vien", length = 100)
    private String nhanVien;
}