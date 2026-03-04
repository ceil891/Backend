package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "hang_khach_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HangKhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hang_id")
    private Integer hangId;

    @Column(name = "ten_hang", length = 50, nullable = false)
    private String tenHang;

    @Column(name = "diem_toi_thieu", nullable = false)
    private Integer diemToiThieu;

    @Column(name = "uu_dai", precision = 5, scale = 2)
    private BigDecimal uuDai;

    @OneToMany(mappedBy = "hang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TheTichDiem> theTichDiems;
}