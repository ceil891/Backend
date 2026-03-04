package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "vai_tro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vai_tro_id")
    private Integer vaiTroId;

    @Column(name = "ten_vai_tro", length = 100, nullable = false, unique = true)
    private String tenVaiTro;

    @ManyToMany(mappedBy = "vaiTros", fetch = FetchType.LAZY)
    private Set<NhanVien> nhanViens;
}