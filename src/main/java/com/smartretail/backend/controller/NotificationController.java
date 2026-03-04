package com.smartretail.backend.controller;

import com.smartretail.backend.dto.notification.NotificationRequest;
import com.smartretail.backend.dto.notification.NotificationResponse;
import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUserNotifications(
            @RequestParam(required = false) Boolean isRead) {
        try {
            List<NotificationResponse> notifications = notificationService.getUserNotifications(isRead);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách thông báo thành công", notifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách thông báo: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(
            @RequestBody NotificationRequest request) {
        try {
            NotificationResponse notification = notificationService.createNotification(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tạo thông báo thành công", notification));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi tạo thông báo: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Integer id) {
        try {
            NotificationResponse notification = notificationService.markAsRead(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Đánh dấu đã đọc thành công", notification));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi đánh dấu đã đọc: " + e.getMessage(), null));
        }
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {
        try {
            notificationService.markAllAsRead();
            return ResponseEntity.ok(new ApiResponse<>(true, "Đánh dấu tất cả đã đọc thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi đánh dấu tất cả đã đọc: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Integer id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Xóa thông báo thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi xóa thông báo: " + e.getMessage(), null));
        }
    }
}
