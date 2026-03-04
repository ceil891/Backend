package com.smartretail.backend.controller;

import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.dto.workshift.WorkShiftRequest;
import com.smartretail.backend.dto.workshift.WorkShiftResponse;
import com.smartretail.backend.service.WorkShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/work-shifts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WorkShiftController {

    private final WorkShiftService workShiftService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkShiftResponse>>> getAllWorkShifts(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate shiftDate) {
        try {
            List<WorkShiftResponse> shifts = workShiftService.getAllWorkShifts(storeId, userId, shiftDate);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách ca làm việc thành công", shifts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách ca làm việc: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkShiftResponse>> getWorkShiftById(@PathVariable Integer id) {
        try {
            return workShiftService.getWorkShiftById(id)
                    .map(shift -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy ca làm việc thành công", shift)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, "Ca làm việc không tồn tại", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy ca làm việc: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WorkShiftResponse>> createWorkShift(@RequestBody WorkShiftRequest request) {
        try {
            if (request.getStoreId() == null || request.getUserId() == null || request.getShiftDate() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "StoreId, UserId và ShiftDate là bắt buộc", null));
            }

            WorkShiftResponse shift = workShiftService.createWorkShift(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tạo ca làm việc thành công", shift));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi tạo ca làm việc: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkShiftResponse>> updateWorkShift(
            @PathVariable Integer id,
            @RequestBody WorkShiftRequest request) {
        try {
            WorkShiftResponse shift = workShiftService.updateWorkShift(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật ca làm việc thành công", shift));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi cập nhật ca làm việc: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkShift(@PathVariable Integer id) {
        try {
            workShiftService.deleteWorkShift(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Xóa ca làm việc thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi xóa ca làm việc: " + e.getMessage(), null));
        }
    }
}
