package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "khuyen_mai")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "khuyen_mai_id")
    private Integer id;

    @Column(name = "ma_khuyen_mai", length = 50, nullable = false, unique = true)
    private String code;

    @Column(name = "ten_chuong_trinh", length = 255, nullable = false)
    private String name;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String description;

    @Column(name = "loai_giam_gia", length = 20, nullable = false)
    private String discountType; // PERCENTAGE / FIXED

    @Column(name = "gia_tri_giam", precision = 10, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @Column(name = "don_toi_thieu", precision = 10, scale = 2)
    private BigDecimal minPurchase;

    @Column(name = "giam_toi_da", precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate startDate;

    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDate endDate;

    @Column(name = "hoat_dong", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

