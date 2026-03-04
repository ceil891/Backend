package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_goi_y")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiGoiY {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goi_y_id")
    private Integer goiYId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private AiAgent agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bien_the_id", nullable = false)
    private BienTheSanPham bienThe;

    @Column(name = "diem_so", nullable = false)
    private Float diemSo;

    @Column(name = "ly_do", columnDefinition = "TEXT")
    private String lyDo;
}