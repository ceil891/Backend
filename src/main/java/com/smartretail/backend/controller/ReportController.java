package com.smartretail.backend.controller;

import com.smartretail.backend.dto.report.ProductSalesReportResponse;
import com.smartretail.backend.dto.report.RevenueReportResponse;
import com.smartretail.backend.dto.report.StoreComparisonResponse;
import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<List<RevenueReportResponse>>> getRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false, defaultValue = "month") String period) {
        try {
            List<RevenueReportResponse> reports = reportService.getRevenueReport(startDate, endDate, storeId, period);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy báo cáo doanh thu thành công", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy báo cáo doanh thu: " + e.getMessage(), null));
        }
    }

    @GetMapping("/product-sales")
    public ResponseEntity<ApiResponse<List<ProductSalesReportResponse>>> getProductSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer storeId) {
        try {
            List<ProductSalesReportResponse> reports = reportService.getProductSalesReport(startDate, endDate, storeId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy báo cáo bán hàng theo sản phẩm thành công", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy báo cáo bán hàng: " + e.getMessage(), null));
        }
    }

    @GetMapping("/store-comparison")
    public ResponseEntity<ApiResponse<List<StoreComparisonResponse>>> getStoreComparison(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<StoreComparisonResponse> reports = reportService.getStoreComparison(startDate, endDate);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy báo cáo so sánh cửa hàng thành công", reports));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy báo cáo so sánh: " + e.getMessage(), null));
        }
    }
}
