package com.smartretail.backend.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Integer id;
    private Integer storeId;
    private String storeName;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private Integer minStock;
    private Integer maxStock;
    private LocalDateTime lastUpdated;
}
