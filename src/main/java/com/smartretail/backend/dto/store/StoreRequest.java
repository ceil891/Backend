package com.smartretail.backend.dto.store;

import lombok.Data;

@Data
public class StoreRequest {
    private String code;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Integer managerId;  // ID người quản lý (optional)
    private Boolean isActive;
}
