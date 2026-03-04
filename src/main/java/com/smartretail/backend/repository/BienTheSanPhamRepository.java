package com.smartretail.backend.repository;

import com.smartretail.backend.entity.BienTheSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BienTheSanPhamRepository extends JpaRepository<BienTheSanPham, Integer> {


    List<BienTheSanPham> findBySanPham_SanPhamId(Integer sanPhamId);

    Optional<BienTheSanPham> findByMaSku(String maSku);

    Optional<BienTheSanPham> findByMaVach(String maVach);

    @Query("SELECT b FROM BienTheSanPham b WHERE b.sanPham.sanPhamId = :sanPhamId AND b.giaBan IS NOT NULL ORDER BY b.giaBan ASC")
    List<BienTheSanPham> findBySanPhamIdOrderByGiaBanAsc(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT b FROM BienTheSanPham b WHERE b.maSku LIKE %:keyword% OR b.tenBienThe LIKE %:keyword%")
    List<BienTheSanPham> searchByKeyword(@Param("keyword") String keyword);
}