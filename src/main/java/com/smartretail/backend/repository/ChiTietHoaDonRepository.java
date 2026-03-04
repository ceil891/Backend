package com.smartretail.backend.repository;

import com.smartretail.backend.entity.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer> {

    List<ChiTietHoaDon> findByHoaDon_HoaDonId(Integer hoaDonId);
}

