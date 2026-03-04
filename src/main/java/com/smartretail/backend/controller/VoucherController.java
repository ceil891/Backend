package com.smartretail.backend.controller;

import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.dto.voucher.VoucherRequest;
import com.smartretail.backend.dto.voucher.VoucherResponse;
import com.smartretail.backend.dto.voucher.VoucherValidationRequest;
import com.smartretail.backend.dto.voucher.VoucherValidationResponse;
import com.smartretail.backend.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/voucher")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VoucherResponse>>> getAllVouchers() {
        try {
            List<VoucherResponse> vouchers = voucherService.getAllVouchers();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách voucher thành công", vouchers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách voucher: " + e.getMessage(), null));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<VoucherResponse>>> getActiveVouchers() {
        try {
            List<VoucherResponse> vouchers = voucherService.getActiveVouchers();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách voucher đang hoạt động thành công", vouchers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách voucher: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VoucherResponse>> getVoucherById(@PathVariable Integer id) {
        try {
            return voucherService.getVoucherById(id)
                    .map(voucher -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy voucher thành công", voucher)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, "Voucher không tồn tại", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy voucher: " + e.getMessage(), null));
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<VoucherResponse>> getVoucherByCode(@PathVariable String code) {
        try {
            return voucherService.getVoucherByCode(code)
                    .map(voucher -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy voucher thành công", voucher)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, "Voucher không tồn tại", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy voucher: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherResponse>> createVoucher(@RequestBody VoucherRequest request) {
        try {
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Mã voucher là bắt buộc", null));
            }
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Tên voucher là bắt buộc", null));
            }

            VoucherResponse voucher = voucherService.createVoucher(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tạo voucher thành công", voucher));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi tạo voucher: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VoucherResponse>> updateVoucher(
            @PathVariable Integer id,
            @RequestBody VoucherRequest request) {
        try {
            VoucherResponse voucher = voucherService.updateVoucher(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật voucher thành công", voucher));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi cập nhật voucher: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVoucher(@PathVariable Integer id) {
        try {
            voucherService.deleteVoucher(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Xóa voucher thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi xóa voucher: " + e.getMessage(), null));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<VoucherValidationResponse>> validateVoucher(
            @RequestBody VoucherValidationRequest request) {
        try {
            VoucherValidationResponse response = voucherService.validateVoucher(request);
            return ResponseEntity.ok(new ApiResponse<>(true, response.getMessage(), response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi validate voucher: " + e.getMessage(), null));
        }
    }
}
