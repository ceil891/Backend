package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "khu_vuc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhuVuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "khu_vuc_id")
    private Integer khuVucId;

    @Column(name = "ten_khu_vuc", length = 100, nullable = false)
    private String tenKhuVuc;

    @OneToMany(mappedBy = "khuVuc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CuaHang> cuaHangs;
}