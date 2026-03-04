package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ai_agent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_id")
    private Integer agentId;

    @Column(name = "ten_agent", length = 100, nullable = false)
    private String tenAgent;

    @Column(name = "loai_agent", length = 50)
    private String loaiAgent;

    @Column(name = "mo_hinh", length = 100)
    private String moHinh;

    @Column(name = "phien_ban", length = 50)
    private String phienBan;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AiHoiThoai> aiHoiThoais;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AiGoiY> aiGoiYs;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AiDuDoan> aiDuDoans;
}