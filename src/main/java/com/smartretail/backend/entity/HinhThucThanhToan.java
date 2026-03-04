package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "hinh_thuc_thanh_toan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhThucThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hinh_thuc_id")
    private Integer hinhThucId;

    @Column(name = "ten_hinh_thuc", length = 50, nullable = false, unique = true)
    private String tenHinhThuc;

    @OneToMany(mappedBy = "hinhThuc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiaoDichThanhToan> giaoDichThanhToans;
}