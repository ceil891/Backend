package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "danh_muc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "danh_muc_id")
    private Integer danhMucId;

    @Column(name = "ten_danh_muc", length = 200, nullable = false)
    private String tenDanhMuc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danh_muc_cha_id")
    // ✅ THÊM DÒNG NÀY: Khi xem danh mục, không quét ngược lại danh sách con của danh mục cha
    @JsonIgnoreProperties("danhMucCons") 
    private DanhMuc danhMucCha;

    @OneToMany(mappedBy = "danhMucCha", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // ✅ THÊM DÒNG NÀY: Khi xem danh sách con, không quét ngược lại thông tin danh mục cha gây lặp
    @JsonIgnoreProperties("danhMucCha")
    private List<DanhMuc> danhMucCons;

    @JsonIgnore 
    @OneToMany(mappedBy = "danhMuc", cascade = CascadeType.ALL)
    private List<SanPham> sanPhams;
}