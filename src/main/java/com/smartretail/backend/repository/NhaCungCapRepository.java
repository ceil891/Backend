package com.smartretail.backend.repository;

import com.smartretail.backend.entity.NhaCungCap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer> {

    @Query("SELECT n FROM NhaCungCap n " +
           "WHERE (:keyword IS NULL OR :keyword = '' " +
           "   OR LOWER(n.tenNhaCungCap) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(n.dienThoai) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(n.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<NhaCungCap> search(@Param("keyword") String keyword);
}

