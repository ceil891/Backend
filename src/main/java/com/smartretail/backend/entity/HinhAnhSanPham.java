package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hinh_anh_san_pham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hinh_anh_id")
    private Integer hinhAnhId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    @Column(name = "duong_dan", columnDefinition = "TEXT", nullable = false)
    private String duongDan;

    @Column(name = "la_chinh", nullable = false)
    private Boolean laChinh = false;
}