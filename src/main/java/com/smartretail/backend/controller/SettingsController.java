package com.smartretail.backend.controller;

import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.dto.settings.ChangePasswordRequest;
import com.smartretail.backend.dto.settings.NotificationSettingsRequest;
import com.smartretail.backend.dto.settings.UpdateProfileRequest;
import com.smartretail.backend.dto.user.UserResponse;
import com.smartretail.backend.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/me")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        try {
            UserResponse user = settingsService.getCurrentUser();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy thông tin user thành công", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy thông tin user: " + e.getMessage(), null));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@RequestBody UpdateProfileRequest request) {
        try {
            UserResponse user = settingsService.updateProfile(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật thông tin thành công", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi cập nhật thông tin: " + e.getMessage(), null));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            if (request.getCurrentPassword() == null || request.getNewPassword() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Mật khẩu hiện tại và mật khẩu mới là bắt buộc", null));
            }
            if (request.getNewPassword().length() < 6) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Mật khẩu mới phải có ít nhất 6 ký tự", null));
            }

            settingsService.changePassword(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Đổi mật khẩu thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi đổi mật khẩu: " + e.getMessage(), null));
        }
    }

    @GetMapping("/settings")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> getNotificationSettings() {
        try {
            Map<String, Boolean> settings = settingsService.getNotificationSettings();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy cài đặt thành công", settings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy cài đặt: " + e.getMessage(), null));
        }
    }

    @PutMapping("/settings")
    public ResponseEntity<ApiResponse<Void>> updateNotificationSettings(
            @RequestBody NotificationSettingsRequest request) {
        try {
            settingsService.updateNotificationSettings(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật cài đặt thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi cập nhật cài đặt: " + e.getMessage(), null));
        }
    }
}
