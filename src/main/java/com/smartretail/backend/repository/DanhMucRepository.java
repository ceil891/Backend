package com.smartretail.backend.repository;

import com.smartretail.backend.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {

    List<DanhMuc> findByDanhMucChaIsNull();

    List<DanhMuc> findByDanhMucCha_DanhMucId(Integer danhMucChaId);

    Optional<DanhMuc> findByTenDanhMuc(String tenDanhMuc);

    @Query("SELECT d FROM DanhMuc d WHERE d.tenDanhMuc LIKE %:keyword%")
    List<DanhMuc> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT d FROM DanhMuc d WHERE d.danhMucCha IS NULL ORDER BY d.tenDanhMuc")
    List<DanhMuc> findRootCategoriesOrdered();
}