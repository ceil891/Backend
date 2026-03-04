package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "shipper")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipper_id")
    private Integer shipperId;

    @Column(name = "ten_shipper", length = 200, nullable = false)
    private String tenShipper;

    @Column(name = "dien_thoai", length = 20)
    private String dienThoai;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @OneToMany(mappedBy = "shipper", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GiaoHang> giaoHangs;
}