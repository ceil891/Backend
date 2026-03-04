package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "giao_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiaoHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giao_hang_id")
    private Integer giaoHangId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipper_id")
    private Shipper shipper;

    @Column(name = "dia_chi_giao", columnDefinition = "TEXT", nullable = false)
    private String diaChiGiao;

    @Column(name = "phi_ship", precision = 10, scale = 2)
    private BigDecimal phiShip;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;
}