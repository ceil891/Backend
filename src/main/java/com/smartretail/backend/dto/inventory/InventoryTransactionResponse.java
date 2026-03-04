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
public class InventoryTransactionResponse {
    private Integer id;
    private Integer storeId;
    private String storeName;
    private Integer productId;
    private String productName;
    private String type;
    private Integer quantity;
    private Integer fromStoreId;
    private String fromStoreName;
    private Integer toStoreId;
    private String toStoreName;
    private String reason;
    private Long createdBy;
    private LocalDateTime createdAt;
}
