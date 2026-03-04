package com.smartretail.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "san_pham")
@Data
@NoArgsConstructor
@AllArgsConstructor
// ✅ Thêm cái này để tránh lỗi khi dùng FetchType.LAZY
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "san_pham_id")
    private Integer sanPhamId;

    @Column(name = "ma_sku", length = 50, unique = true)
    private String maSku;

    @Column(name = "ten_san_pham", length = 255, nullable = false)
    private String tenSanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danh_muc_id", nullable = false)
    // ✅ QUAN TRỌNG: Chặn không cho lấy ngược lại List<SanPham> từ DanhMuc gây lặp
    @JsonIgnoreProperties("sanPhams") 
    private DanhMuc danhMuc;

    @Column(name = "don_vi_id", nullable = false)
    private Integer donViId;

    @ElementCollection
    @CollectionTable(name = "san_pham_hinh_anh", joinColumns = @JoinColumn(name = "san_pham_id"))
    @Column(name = "hinh_anh_url")
    private List<String> hinhAnhUrls;

    @Column(name = "thuong_hieu", length = 100)
    private String thuongHieu;

    @Column(name = "gia_ban", precision = 10, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "gia_nhap", precision = 10, scale = 2)
    private BigDecimal giaNhap;

    @Column(name = "ma_vach", length = 50)
    private String maVach;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "hoat_dong", nullable = false)
    private Boolean hoatDong = true;

    // ✅ Thêm @JsonIgnore để danh sách sản phẩm không bị phình to bởi biến thể
    @JsonIgnore 
    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BienTheSanPham> bienTheSanPhams;

    // ✅ Tương tự cho phần đánh giá
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DanhGiaSanPham> danhGiaSanPhams;
}