package com.smartretail.backend.repository;

import com.smartretail.backend.entity.HinhAnhSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HinhAnhSanPhamRepository extends JpaRepository<HinhAnhSanPham, Integer> {

    List<HinhAnhSanPham> findBySanPham_SanPhamId(Integer sanPhamId);
}
