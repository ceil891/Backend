package com.smartretail.backend.dto.user;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String password;      // Chỉ cần khi tạo mới hoặc đổi mật khẩu
    private String fullName;
    private String phone;
    private String role;           // CUSTOMER, STAFF, MANAGER, ADMIN, SUPER_ADMIN
    private Integer storeId;       // null nếu là ADMIN/SUPER_ADMIN
    private Boolean isActive;
}
