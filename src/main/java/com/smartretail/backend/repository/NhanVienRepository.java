package com.smartretail.backend.repository;

import com.smartretail.backend.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {

    Optional<NhanVien> findByEmail(String email);

    List<NhanVien> findByCuaHang_CuaHangId(Integer cuaHangId);

    List<NhanVien> findByTrangThai(String trangThai);

    @Query("SELECT n FROM NhanVien n WHERE n.hoTen LIKE %:keyword% OR n.email LIKE %:keyword% OR n.dienThoai LIKE %:keyword%")
    List<NhanVien> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT n FROM NhanVien n JOIN n.vaiTros v WHERE v.tenVaiTro = :roleName")
    List<NhanVien> findByRole(@Param("roleName") String roleName);

    @Query("SELECT n FROM NhanVien n WHERE n.cuaHang.cuaHangId = :cuaHangId AND n.trangThai = 'active'")
    List<NhanVien> findActiveEmployeesByStore(@Param("cuaHangId") Integer cuaHangId);
}