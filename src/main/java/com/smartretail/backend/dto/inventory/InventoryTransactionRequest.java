package com.smartretail.backend.dto.inventory;

import lombok.Data;

@Data
public class InventoryTransactionRequest {
    private Integer storeId;
    private Integer productId;
    private String type;          // IMPORT, EXPORT, TRANSFER, ADJUSTMENT
    private Integer quantity;     // Dương cho nhập, âm cho xuất
    private Integer fromStoreId;  // Chỉ có khi type = TRANSFER
    private Integer toStoreId;    // Chỉ có khi type = TRANSFER
    private String reason;
}
