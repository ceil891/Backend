package com.smartretail.backend.service;

import com.smartretail.backend.dto.dashboard.DashboardStatsResponse;
import com.smartretail.backend.repository.HoaDonRepository;
import com.smartretail.backend.repository.SanPhamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Timestamp;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final HoaDonRepository hoaDonRepository;
    private final SanPhamRepository sanPhamRepository;

    // Tránh phụ thuộc vào Lombok để loại bỏ lỗi "Unresolved compilation problem"
    public DashboardService(HoaDonRepository hoaDonRepository, SanPhamRepository sanPhamRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    public DashboardStatsResponse getStats(Integer storeId, LocalDate startDate, LocalDate endDate) {
        // Chuẩn hóa khoảng ngày
        LocalDate effectiveStart = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate effectiveEnd = endDate != null ? endDate : LocalDate.now();

        Timestamp startTs = Timestamp.valueOf(effectiveStart.atStartOfDay());
        Timestamp endTs = Timestamp.valueOf(effectiveEnd.plusDays(1).atStartOfDay().minusNanos(1));

        // Tính tổng doanh thu (dựa trên tongPhaiThanhToan)
        BigDecimal totalRevenue = BigDecimal.ZERO;
        if (storeId == null) {
            Double revenue = hoaDonRepository.getTotalRevenueInPeriod(startTs, endTs);
            if (revenue != null) {
                totalRevenue = BigDecimal.valueOf(revenue);
            }
        } else {
            // Nếu cần theo cửa hàng cụ thể thì dùng findAll rồi filter, tránh viết thêm query phức tạp
            totalRevenue = hoaDonRepository.findAll().stream()
                    .filter(hd -> hd.getCuaHang() != null && hd.getCuaHang().getCuaHangId().equals(storeId))
                    .map(hd -> hd.getTongPhaiThanhToan() != null ? hd.getTongPhaiThanhToan() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        // Tổng số đơn hàng
        long totalOrders = (storeId == null)
                ? hoaDonRepository.count()
                : hoaDonRepository.findByCuaHang_CuaHangId(storeId).size();

        // Tổng số sản phẩm
        long totalProducts = sanPhamRepository.count();

        // Sản phẩm sắp hết hàng (mock - cần tích hợp với Inventory)
        long lowStockProducts = 12L;  // TODO: Tính từ Inventory

        // Đề xuất chưa xử lý (mock - cần tích hợp với AI)
        long pendingRecommendations = 5L;  // TODO: Tính từ AIRecommendation

        // Tính % tăng trưởng (mock - cần so sánh với kỳ trước)
        double revenueGrowth = 15.5;
        double orderGrowth = 8.2;

        return DashboardStatsResponse.builder()
                .totalRevenue(totalRevenue)
                .totalOrders(totalOrders)
                .totalProducts(totalProducts)
                .lowStockProducts(lowStockProducts)
                .pendingRecommendations(pendingRecommendations)
                .revenueGrowth(revenueGrowth)
                .orderGrowth(orderGrowth)
                .build();
    }
}
