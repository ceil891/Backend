package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_du_doan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiDuDoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "du_doan_id")
    private Integer duDoanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private AiAgent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cua_hang_id", nullable = false)
    private CuaHang cuaHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bien_the_id", nullable = false)
    private BienTheSanPham bienThe;

    @Column(name = "loai_du_doan", length = 50, nullable = false)
    private String loaiDuDoan;

    @Column(name = "gia_tri", nullable = false)
    private Float giaTri;

    @Column(name = "khoang_thoi_gian", length = 50)
    private String khoangThoiGian;
}