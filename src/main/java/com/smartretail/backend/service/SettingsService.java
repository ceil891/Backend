package com.smartretail.backend.service;

import com.smartretail.backend.dto.settings.ChangePasswordRequest;
import com.smartretail.backend.dto.settings.NotificationSettingsRequest;
import com.smartretail.backend.dto.settings.UpdateProfileRequest;
import com.smartretail.backend.dto.user.UserResponse;
import com.smartretail.backend.entity.User;
import com.smartretail.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class SettingsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        return userService.getUserById(user.getId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    public UserResponse updateProfile(UpdateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            user.setPhoneNumber(request.getPhone());
        }

        User saved = userRepository.save(user);
        return userService.getUserById(saved.getId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public Map<String, Boolean> getNotificationSettings() {
        // TODO: Lưu vào database hoặc cache
        // Tạm thời trả về default
        Map<String, Boolean> settings = new HashMap<>();
        settings.put("emailNotifications", true);
        settings.put("smsNotifications", false);
        settings.put("lowStockAlerts", true);
        settings.put("aiRecommendations", true);
        return settings;
    }

    public void updateNotificationSettings(NotificationSettingsRequest request) {
        // TODO: Lưu vào database hoặc cache
        // Tạm thời chỉ log
    }
}
