package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "the_tich_diem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheTichDiem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "the_id")
    private Integer theId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hang_id", nullable = false)
    private HangKhachHang hang;

    @Column(name = "tong_diem", nullable = false)
    private Integer tongDiem;

    @OneToMany(mappedBy = "the", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LichSuDiem> lichSuDiems;
}