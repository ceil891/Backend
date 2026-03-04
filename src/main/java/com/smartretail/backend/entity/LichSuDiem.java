package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "lich_su_diem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichSuDiem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lich_su_id")
    private Integer lichSuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "the_id", nullable = false)
    private TheTichDiem the;

    @Column(name = "thay_doi", nullable = false)
    private Integer thayDoi;

    @Column(name = "ly_do", columnDefinition = "TEXT")
    private String lyDo;

    @Column(name = "ngay_tao", nullable = false)
    private Timestamp ngayTao;
}