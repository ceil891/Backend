package com.smartretail.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; // ✅ Bắt buộc import
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_hoa_don")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chi_tiet_id")
    private Integer chiTietId;

    // ✅ CHẶN LỖI LẶP VÔ TẬN: Không gửi ngược HoaDon về nữa
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private HoaDon hoaDon;

    // ✅ QUAN TRỌNG NHẤT: Đổi từ LAZY thành EAGER để tự động lấy được Tên Sản Phẩm!
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "bien_the_id", nullable = false)
    private BienTheSanPham bienThe;

    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @Column(name = "don_gia", precision = 10, scale = 2, nullable = false)
    private BigDecimal donGia;

    @Column(name = "thanh_tien", precision = 10, scale = 2, nullable = false)
    private BigDecimal thanhTien;
}