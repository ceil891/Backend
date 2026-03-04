package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "ai_tin_nhan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiTinNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tin_nhan_id")
    private Integer tinNhanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoi_thoai_id", nullable = false)
    private AiHoiThoai hoiThoai;

    @Column(name = "nguoi_gui", length = 10, nullable = false)
    private String nguoiGui; // "user" hoặc "agent"

    @Column(name = "noi_dung", columnDefinition = "TEXT", nullable = false)
    private String noiDung;

    @Column(name = "thoi_gian", nullable = false)
    private Timestamp thoiGian;
}