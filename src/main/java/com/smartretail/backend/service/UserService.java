package com.smartretail.backend.service;

import com.smartretail.backend.dto.user.UserRequest;
import com.smartretail.backend.dto.user.UserResponse;
import com.smartretail.backend.entity.CuaHang;
import com.smartretail.backend.entity.Role;
import com.smartretail.backend.entity.User;
import com.smartretail.backend.enums.RoleName;
import com.smartretail.backend.repository.CuaHangRepository;
import com.smartretail.backend.repository.RoleRepository;
import com.smartretail.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CuaHangRepository cuaHangRepository; 
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers(String role, Integer storeId) {
        List<User> users = userRepository.findAll();
        
        return users.stream()
                .filter(user -> role == null || user.getRoles().stream()
                        .anyMatch(r -> r.getName().name().equals(role)))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse);
    }

    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Mật khẩu là bắt buộc");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhone());
        user.setEnabled(request.getIsActive() != null ? request.getIsActive() : true);

        if (request.getStoreId() != null) {
            CuaHang store = cuaHangRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Cửa hàng không tồn tại"));
            user.setCuaHang(store);
        }

        Set<Role> roles = new HashSet<>();
        RoleName roleName = RoleName.valueOf(request.getRole().toUpperCase());
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại: " + request.getRole()));
        roles.add(role);
        user.setRoles(roles);

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhone());
        user.setEnabled(request.getIsActive() != null ? request.getIsActive() : user.isEnabled());

        if (request.getStoreId() != null) {
            CuaHang store = cuaHangRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Cửa hàng không tồn tại"));
            user.setCuaHang(store);
        } else {
            user.setCuaHang(null); 
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRole() != null) {
            Set<Role> roles = new HashSet<>();
            RoleName roleName = RoleName.valueOf(request.getRole().toUpperCase());
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role không tồn tại: " + request.getRole()));
            roles.add(role);
            user.setRoles(roles);
        }

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse toggleActive(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        user.setEnabled(!user.isEnabled());
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        userRepository.delete(user);
    }

    private UserResponse toResponse(User user) {
        String role = user.getRoles().stream()
                .findFirst()
                .map(r -> r.getName().name())
                .orElse("ROLE_STAFF");

        // ✅ SỬA TỪ getId() THÀNH getCuaHangId() ĐỂ KHỚP VỚI ENTITY CUA HÀNG
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhoneNumber())
                .role(role)
                .storeId(user.getCuaHang() != null ? user.getCuaHang().getCuaHangId() : null)
                .isActive(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}