package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "giao_dich_thanh_toan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiaoDichThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giao_dich_id")
    private Integer giaoDichId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hinh_thuc_id", nullable = false)
    private HinhThucThanhToan hinhThuc;

    @Column(name = "so_tien", precision = 10, scale = 2, nullable = false)
    private BigDecimal soTien;

    @Column(name = "thoi_gian", nullable = false)
    private Timestamp thoiGian;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;
}