package com.smartretail.backend.repository;

import com.smartretail.backend.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    // --- CÁC HÀM CŨ CỦA BẠN (GIỮ NGUYÊN) ---
    List<HoaDon> findByCuaHang_CuaHangId(Integer cuaHangId);
    List<HoaDon> findByKhachHang_KhachHangId(Integer khachHangId);
    List<HoaDon> findByNhanVien_NhanVienId(Integer nhanVienId);
    List<HoaDon> findByTrangThai(String trangThai);

    @Query("SELECT h FROM HoaDon h WHERE h.ngayLap BETWEEN :startDate AND :endDate")
    List<HoaDon> findByDateRange(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT h FROM HoaDon h WHERE h.cuaHang.cuaHangId = :cuaHangId AND h.ngayLap BETWEEN :startDate AND :endDate")
    List<HoaDon> findByCuaHangAndDateRange(@Param("cuaHangId") Integer cuaHangId,
                                           @Param("startDate") Timestamp startDate,
                                           @Param("endDate") Timestamp endDate);

    @Query("SELECT SUM(h.tongPhaiThanhToan) FROM HoaDon h WHERE h.trangThai = 'completed' AND h.ngayLap BETWEEN :startDate AND :endDate")
    Double getTotalRevenueInPeriod(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("""
            SELECT h
            FROM HoaDon h
            LEFT JOIN h.khachHang k
            WHERE (:storeId IS NULL OR h.cuaHang.cuaHangId = :storeId)
              AND (:channel IS NULL OR h.kenhBan = :channel)
              AND (:status IS NULL OR h.trangThai = :status)
              AND (:fromDate IS NULL OR h.ngayLap >= :fromDate)
              AND (:toDate IS NULL OR h.ngayLap <= :toDate)
              AND (
                    :keyword IS NULL OR :keyword = '' OR
                    LOWER(COALESCE(h.maHoaDon, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    (k IS NOT NULL AND (
                        LOWER(COALESCE(k.hoTen, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                        LOWER(COALESCE(k.dienThoai, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    ))
              )
            ORDER BY h.ngayLap DESC
            """)
    List<HoaDon> searchOrders(
            @Param("storeId") Integer storeId,
            @Param("channel") String channel,
            @Param("status") String status,
            @Param("fromDate") Timestamp fromDate,
            @Param("toDate") Timestamp toDate,
            @Param("keyword") String keyword
    );


    // --- 3 HÀM BÁO CÁO MỚI CHO REACT RECHARTS ---

    // 1. DOANH THU THEO THỜI GIAN
    @Query(value = "SELECT " +
                   "  DATE_FORMAT(hd.ngay_lap, '%d/%m/%Y') AS period, " +
                   "  SUM(hd.tong_phai_thanh_toan) AS revenue, " +
                   "  COUNT(*) AS orders, " +
                   "  SUM(hd.tong_phai_thanh_toan * 0.3) AS profit " + 
                   "FROM hoa_don hd " +
                   "WHERE hd.ngay_lap >= :startDate AND hd.ngay_lap <= :endDate " +
                   "AND hd.trang_thai = 'completed' " +
                   "GROUP BY DATE_FORMAT(hd.ngay_lap, '%d/%m/%Y') " +
                   "ORDER BY MIN(hd.ngay_lap)", nativeQuery = true)
    List<Object[]> getRevenueReportRaw(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // 2. BÁN HÀNG THEO SẢN PHẨM (Top 5)
    @Query(value = "SELECT " +
                   "  sp.san_pham_id AS productId, " +
                   "  sp.ten_san_pham AS productName, " +
                   "  SUM(ct.so_luong) AS quantitySold, " +
                   "  SUM(ct.thanh_tien) AS revenue, " +
                   "  SUM(ct.thanh_tien * 0.3) AS profit " +
                   "FROM chi_tiet_hoa_don ct " +
                   "JOIN hoa_don hd ON ct.hoa_don_id = hd.hoa_don_id " +
                   "JOIN bien_the_san_pham bt ON ct.bien_the_id = bt.bien_the_id " +
                   "JOIN san_pham sp ON bt.san_pham_id = sp.san_pham_id " +
                   "WHERE hd.ngay_lap >= :startDate AND hd.ngay_lap <= :endDate " +
                   "AND hd.trang_thai = 'completed' " +
                   "GROUP BY sp.san_pham_id, sp.ten_san_pham " +
                   "ORDER BY revenue DESC LIMIT 5", nativeQuery = true)
    List<Object[]> getProductSalesReportRaw(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // 3. SO SÁNH CỬA HÀNG
    @Query(value = "SELECT " +
                   "  ch.cua_hang_id AS storeId, " +
                   "  ch.ten_cua_hang AS storeName, " +
                   "  SUM(hd.tong_phai_thanh_toan) AS revenue, " +
                   "  COUNT(*) AS orders " +
                   "FROM hoa_don hd " +
                   "JOIN cua_hang ch ON hd.cua_hang_id = ch.cua_hang_id " +
                   "WHERE hd.ngay_lap >= :startDate AND hd.ngay_lap <= :endDate " +
                   "AND hd.trang_thai = 'completed' " +
                   "GROUP BY ch.cua_hang_id, ch.ten_cua_hang " +
                   "ORDER BY revenue DESC", nativeQuery = true)
    List<Object[]> getStoreComparisonRaw(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}