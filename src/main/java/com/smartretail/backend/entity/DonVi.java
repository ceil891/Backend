package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "don_vi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonVi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "don_vi_id")
    private Integer donViId;

    @Column(name = "ten_don_vi", length = 50, nullable = false)
    private String tenDonVi;

}