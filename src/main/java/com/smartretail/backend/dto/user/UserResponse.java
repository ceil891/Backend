package com.smartretail.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private Integer storeId;
    private Boolean isActive;
    private String avatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
