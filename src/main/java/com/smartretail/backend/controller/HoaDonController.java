package com.smartretail.backend.controller;

import com.smartretail.backend.dto.HoaDonDTO;
import com.smartretail.backend.dto.pos.CreateOrderRequest;
import com.smartretail.backend.dto.pos.OrderSummaryDTO;
import com.smartretail.backend.entity.ChiTietHoaDon;
import com.smartretail.backend.entity.HoaDon;
import com.smartretail.backend.service.ExcelExportService;
import com.smartretail.backend.service.GoogleSheetsService;
import com.smartretail.backend.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/hoa-don")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HoaDonController {

    private final HoaDonService hoaDonService;
    private final ExcelExportService excelExportService;
    private final GoogleSheetsService googleSheetsService;

    // ✅ Đã sửa: Gọi thẳng hàm getAllHoaDon từ Service (hàm này đã được đổi để trả về List<HoaDonDTO>)
    @GetMapping
    public ResponseEntity<List<HoaDonDTO>> getAllHoaDon() { 
        List<HoaDonDTO> dtos = hoaDonService.getAllHoaDonDTOs();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoaDonDTO> getHoaDonById(@PathVariable Integer id) {
        Optional<HoaDon> hoaDonOpt = hoaDonService.getHoaDonById(id);
        if (hoaDonOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        HoaDon hoaDon = hoaDonOpt.get();
        List<ChiTietHoaDon> chiTietList = hoaDon.getChiTietHoaDons();
        HoaDonDTO dto = hoaDonService.mapToHoaDonDTO(hoaDon, chiTietList);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cua-hang/{cuaHangId}")
    public ResponseEntity<List<HoaDon>> getHoaDonByCuaHang(@PathVariable Integer cuaHangId) {
        List<HoaDon> hoaDons = hoaDonService.getHoaDonByCuaHang(cuaHangId);
        return ResponseEntity.ok(hoaDons);
    }

    @GetMapping("/khach-hang/{khachHangId}")
    public ResponseEntity<List<HoaDon>> getHoaDonByKhachHang(@PathVariable Integer khachHangId) {
        List<HoaDon> hoaDons = hoaDonService.getHoaDonByKhachHang(khachHangId);
        return ResponseEntity.ok(hoaDons);
    }

    @GetMapping("/nhan-vien/{nhanVienId}")
    public ResponseEntity<List<HoaDon>> getHoaDonByNhanVien(@PathVariable Integer nhanVienId) {
        List<HoaDon> hoaDons = hoaDonService.getHoaDonByNhanVien(nhanVienId);
        return ResponseEntity.ok(hoaDons);
    }

    @GetMapping("/trang-thai/{trangThai}")
    public ResponseEntity<List<HoaDon>> getHoaDonByTrangThai(@PathVariable String trangThai) {
        List<HoaDon> hoaDons = hoaDonService.getHoaDonByTrangThai(trangThai);
        return ResponseEntity.ok(hoaDons);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<HoaDon>> getHoaDonByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp endDate) {
        List<HoaDon> hoaDons = hoaDonService.getHoaDonByDateRange(startDate, endDate);
        return ResponseEntity.ok(hoaDons);
    }

    @GetMapping("/cua-hang/{cuaHangId}/date-range")
    public ResponseEntity<List<HoaDon>> getHoaDonByCuaHangAndDateRange(
            @PathVariable Integer cuaHangId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp endDate) {
        List<HoaDon> hoaDons = hoaDonService.getHoaDonByCuaHangAndDateRange(cuaHangId, startDate, endDate);
        return ResponseEntity.ok(hoaDons);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenueInPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp endDate) {
        Double revenue = hoaDonService.getTotalRevenueInPeriod(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/query")
    public ResponseEntity<List<HoaDonDTO>> queryHoaDon(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp to,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(hoaDonService.searchOrders(storeId, channel, status, from, to, keyword));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportHoaDonExcel(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp to,
            @RequestParam(required = false) String keyword
    ) {
        List<HoaDonDTO> orders = hoaDonService.searchOrders(storeId, channel, status, from, to, keyword);

        List<String> headers = List.of(
                "Mã HĐ", "Ngày", "Khách hàng", "SĐT", "Kênh", "Tổng tiền", "Giảm giá", "Thanh toán", "Nhân viên", "Chi nhánh", "Trạng thái"
        );
        List<List<Object>> rows = new ArrayList<>();
        for (HoaDonDTO o : orders) {
            rows.add(List.of(
                    o.getMaHoaDon(),
                    o.getNgayLap(),
                    o.getTenKhachHang(),
                    o.getDienThoaiKhachHang(),
                    o.getKenhBan(),
                    o.getTamTinh(),
                    o.getChietKhau(),
                    o.getTongPhaiThanhToan(),
                    o.getTenNhanVien(),
                    o.getTenCuaHang(),
                    o.getTrangThai()
            ));
        }

        byte[] bytes = excelExportService.exportToExcel("Orders", headers, rows);
        googleSheetsService.appendRow("exports", List.of(
                "EXPORT_ORDERS",
                channel,
                status,
                from,
                to,
                keyword,
                orders.size()
        ));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }

    @PostMapping
    public ResponseEntity<OrderSummaryDTO> createHoaDonFromPos(@RequestBody CreateOrderRequest request) {
        HoaDon saved = hoaDonService.createOrderFromPos(request);
        OrderSummaryDTO dto = hoaDonService.mapToOrderSummary(saved);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HoaDon> updateHoaDon(@PathVariable Integer id, @RequestBody HoaDon hoaDon) {
        hoaDon.setHoaDonId(id);
        HoaDon updatedHoaDon = hoaDonService.updateHoaDon(hoaDon);
        return ResponseEntity.ok(updatedHoaDon);
    }

    @PutMapping("/{id}/trang-thai")
    public ResponseEntity<Void> updateTrangThai(@PathVariable Integer id, @RequestParam String trangThai) {
        hoaDonService.updateTrangThai(id, trangThai);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancel(@PathVariable Integer id,
                                                     @RequestParam(required = false) String reason,
                                                     @RequestParam(required = false) String cancelledBy) {
        hoaDonService.cancelOrder(id, reason, cancelledBy);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHoaDon(@PathVariable Integer id) {
        hoaDonService.deleteHoaDon(id);
        return ResponseEntity.noContent().build();
    }
}