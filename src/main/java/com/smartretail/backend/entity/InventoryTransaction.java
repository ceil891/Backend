package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private CuaHang store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private SanPham product;

    @Column(name = "type", length = 20, nullable = false)
    private String type;  // IMPORT, EXPORT, TRANSFER, ADJUSTMENT

    @Column(name = "quantity", nullable = false)
    private Integer quantity;  // Dương cho nhập, âm cho xuất

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_store_id")
    private CuaHang fromStore;  // Cửa hàng nguồn (nếu là điều chuyển)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_store_id")
    private CuaHang toStore;  // Cửa hàng đích (nếu là điều chuyển)

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;  // User ID

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
