package com.smartretail.backend.repository;

import com.smartretail.backend.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    Optional<KhachHang> findByDienThoai(String dienThoai);

    Optional<KhachHang> findByEmail(String email);

    List<KhachHang> findByTrangThai(String trangThai);

    @Query("SELECT k FROM KhachHang k WHERE k.hoTen LIKE %:keyword% OR k.dienThoai LIKE %:keyword% OR k.email LIKE %:keyword%")
    List<KhachHang> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(k) FROM KhachHang k WHERE MONTH(k.ngayTao) = :month AND YEAR(k.ngayTao) = :year")
    Long countNewCustomersInMonth(@Param("month") int month, @Param("year") int year);
}