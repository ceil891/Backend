package com.smartretail.backend.repository;

import com.smartretail.backend.entity.CuaHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuaHangRepository extends JpaRepository<CuaHang, Integer> {

    List<CuaHang> findByKhuVuc_KhuVucId(Integer khuVucId);

    List<CuaHang> findByTrangThai(String trangThai);

    @Query("SELECT c FROM CuaHang c WHERE c.tenCuaHang LIKE %:keyword% OR c.diaChi LIKE %:keyword%")
    List<CuaHang> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT c FROM CuaHang c WHERE c.khuVuc.tenKhuVuc = :khuVucName")
    List<CuaHang> findByKhuVucName(@Param("khuVucName") String khuVucName);
}