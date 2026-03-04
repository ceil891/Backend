package com.smartretail.backend.repository;

import com.smartretail.backend.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    List<SanPham> findByDanhMuc_DanhMucId(Integer danhMucId);

    List<SanPham> findByThuongHieu(String thuongHieu);

    List<SanPham> findByHoatDongTrue();

    Optional<SanPham> findByMaSku(String maSku);

    @Query("SELECT s FROM SanPham s WHERE s.tenSanPham LIKE %:keyword% OR s.maSku LIKE %:keyword%")
    List<SanPham> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT s FROM SanPham s WHERE s.danhMuc.danhMucId = :danhMucId AND s.hoatDong = true")
    List<SanPham> findActiveByDanhMuc(@Param("danhMucId") Integer danhMucId);
}