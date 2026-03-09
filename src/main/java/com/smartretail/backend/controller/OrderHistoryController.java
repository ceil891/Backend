package com.smartretail.backend.controller;

import com.smartretail.backend.entity.SoQuy;
import com.smartretail.backend.repository.SoQuyRepository;
import com.smartretail.backend.service.ExcelExportService;
import com.smartretail.backend.service.GoogleSheetsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/order-history")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderHistoryController {

    private final SoQuyRepository soQuyRepository;
    private final ExcelExportService excelExportService;
    private final GoogleSheetsService googleSheetsService;

    @GetMapping

    public ResponseEntity<List<Map<String, Object>>> getHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp to,
            @RequestParam(required = false) String keyword
    ) {
        List<SoQuy> rows = soQuyRepository.search(from, to, keyword);
        List<Map<String, Object>> out = rows.stream().map(s -> {
            BigDecimal signed = s.getSoTien() == null ? BigDecimal.ZERO :
                    ("chi".equalsIgnoreCase(s.getLoai()) ? s.getSoTien().negate() : s.getSoTien());
            String loaiGD = "chi".equalsIgnoreCase(s.getLoai()) ? "Trả hàng" : "Bán hàng";

            // Dùng HashMap truyền thống để chấp nhận giá trị Null an toàn
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", s.getSoQuyId());
            map.put("maGD", s.getThamChieu() != null ? s.getThamChieu() : ("SQ-" + s.getSoQuyId()));
            map.put("thoiGian", s.getThoiGian());
            map.put("loaiGD", loaiGD);
            map.put("doiTuong", s.getDoiTuong() != null ? s.getDoiTuong() : "");
            map.put("giaTri", signed);
            map.put("phuongThuc", s.getPhuongThuc() != null ? s.getPhuongThuc() : "CASH");
            map.put("nhanVien", s.getNhanVien() != null ? s.getNhanVien() : "");

            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(out);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportHistoryExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Timestamp to,
            @RequestParam(required = false) String keyword
    ) {
        List<SoQuy> rowsData = soQuyRepository.search(from, to, keyword);
        List<String> headers = List.of("Mã GD", "Thời gian", "Loại", "Đối tượng", "Giá trị", "Phương thức", "Nhân viên");
        List<List<Object>> rows = new ArrayList<>();
        for (SoQuy s : rowsData) {
            BigDecimal signed = s.getSoTien() == null ? BigDecimal.ZERO :
                    ("chi".equalsIgnoreCase(s.getLoai()) ? s.getSoTien().negate() : s.getSoTien());
            String loaiGD = "chi".equalsIgnoreCase(s.getLoai()) ? "Trả hàng" : "Bán hàng";
            rows.add(List.of(
                    s.getThamChieu() != null ? s.getThamChieu() : ("SQ-" + s.getSoQuyId()),
                    s.getThoiGian(),
                    loaiGD,
                    s.getDoiTuong(),
                    signed,
                    s.getPhuongThuc(),
                    s.getNhanVien()
            ));
        }
        byte[] bytes = excelExportService.exportToExcel("History", headers, rows);
        googleSheetsService.appendRow("exports", List.of(
                "EXPORT_ORDER_HISTORY",
                from,
                to,
                keyword,
                rowsData.size()
        ));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order_history.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }
}

