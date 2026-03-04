package com.smartretail.backend.dto.category;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
    private Integer parentId;  // null nếu là danh mục gốc
    private Boolean isActive;
}
