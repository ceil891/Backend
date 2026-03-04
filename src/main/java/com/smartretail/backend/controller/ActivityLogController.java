package com.smartretail.backend.controller;

import com.smartretail.backend.dto.activitylog.ActivityLogResponse;
import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/activity-logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ActivityLogResponse>>> getActivityLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Page<ActivityLogResponse> logs = activityLogService.getActivityLogs(
                    userId, action, entityType, search, page, size);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách nhật ký thành công", logs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách nhật ký: " + e.getMessage(), null));
        }
    }
}
