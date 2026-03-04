package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "nhan_vien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nhan_vien_id")
    private Integer nhanVienId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cua_hang_id", nullable = false)
    private CuaHang cuaHang;

    @Column(name = "ho_ten", length = 200, nullable = false)
    private String hoTen;

    @Column(name = "dien_thoai", length = 20)
    private String dienThoai;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "mat_khau_hash", columnDefinition = "TEXT", nullable = false)
    private String matKhauHash;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "nhan_vien_vai_tro",
        joinColumns = @JoinColumn(name = "nhan_vien_id"),
        inverseJoinColumns = @JoinColumn(name = "vai_tro_id")
    )
    private Set<VaiTro> vaiTros;

   @OneToMany(mappedBy = "nhanVien")
@JsonIgnore // ✅ Thêm dòng này để khi lấy Hóa đơn, nó không lôi cả danh sách hóa đơn cũ của nhân viên đó ra
private List<HoaDon> hoaDons;
}