package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "ai_hoi_thoai")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiHoiThoai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hoi_thoai_id")
    private Integer hoiThoaiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private AiAgent agent;

    @Column(name = "nguoi_dung_id")
    private Integer nguoiDungId;

    @Column(name = "kenh", length = 50)
    private String kenh;

    @Column(name = "bat_dau")
    private Timestamp batDau;

    @Column(name = "ket_thuc")
    private Timestamp ketThuc;

    @OneToMany(mappedBy = "hoiThoai", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AiTinNhan> aiTinNhans;
}