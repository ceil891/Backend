package com.smartretail.backend.service;

import com.smartretail.backend.dto.report.ProductSalesReportResponse;
import com.smartretail.backend.dto.report.RevenueReportResponse;
import com.smartretail.backend.dto.report.StoreComparisonResponse;
import com.smartretail.backend.repository.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final HoaDonRepository hoaDonRepository;

    public List<RevenueReportResponse> getRevenueReport(
            LocalDate startDate, LocalDate endDate, Integer storeId, String period) {
        
        // Chuyển sang java.sql.Timestamp để khớp với Repository
        Timestamp start = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp end = Timestamp.valueOf(endDate.atTime(LocalTime.MAX));

        List<Object[]> rawData = hoaDonRepository.getRevenueReportRaw(start, end);
        List<RevenueReportResponse> reports = new ArrayList<>();

        for (Object[] row : rawData) {
            String rowPeriod = (String) row[0];
            BigDecimal revenue = new BigDecimal(row[1] != null ? row[1].toString() : "0");
            Long orders = ((Number) row[2]).longValue();
            BigDecimal profit = new BigDecimal(row[3] != null ? row[3].toString() : "0");
            
            BigDecimal avgOrderValue = orders > 0 
                ? revenue.divide(BigDecimal.valueOf(orders), RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

            reports.add(RevenueReportResponse.builder()
                    .period(rowPeriod)
                    .revenue(revenue)
                    .orders(orders)
                    .averageOrderValue(avgOrderValue)
                    .profit(profit)
                    .profitMargin(30.0) 
                    .build());
        }
        return reports;
    }

    public List<ProductSalesReportResponse> getProductSalesReport(
            LocalDate startDate, LocalDate endDate, Integer storeId) {
        
        Timestamp start = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp end = Timestamp.valueOf(endDate.atTime(LocalTime.MAX));

        List<Object[]> rawData = hoaDonRepository.getProductSalesReportRaw(start, end);
        List<ProductSalesReportResponse> reports = new ArrayList<>();

        for (Object[] row : rawData) {
            reports.add(ProductSalesReportResponse.builder()
                    .productId(((Number) row[0]).intValue())
                    .productName((String) row[1])
                    .quantitySold(((Number) row[2]).intValue())
                    .revenue(new BigDecimal(row[3] != null ? row[3].toString() : "0"))
                    .profit(new BigDecimal(row[4] != null ? row[4].toString() : "0"))
                    .build());
        }
        return reports;
    }

    public List<StoreComparisonResponse> getStoreComparison(
            LocalDate startDate, LocalDate endDate) {
        
        Timestamp start = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp end = Timestamp.valueOf(endDate.atTime(LocalTime.MAX));

        List<Object[]> rawData = hoaDonRepository.getStoreComparisonRaw(start, end);
        List<StoreComparisonResponse> reports = new ArrayList<>();

        for (Object[] row : rawData) {
            BigDecimal revenue = new BigDecimal(row[2] != null ? row[2].toString() : "0");
            Long orders = ((Number) row[3]).longValue();
            
            BigDecimal avgOrderValue = orders > 0 
                ? revenue.divide(BigDecimal.valueOf(orders), RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

            reports.add(StoreComparisonResponse.builder()
                    .storeId(((Number) row[0]).intValue())
                    .storeName((String) row[1])
                    .revenue(revenue)
                    .orders(orders)
                    .averageOrderValue(avgOrderValue)
                    .growth(5.5) // Tạm mock tỷ lệ tăng trưởng
                    .build());
        }
        return reports;
    }
}