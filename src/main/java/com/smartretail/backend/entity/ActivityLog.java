package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "action", length = 100, nullable = false)
    private String action;  // CREATE_ORDER, UPDATE_INVENTORY, DELETE_PRODUCT...

    @Column(name = "entity_type", length = 50, nullable = false)
    private String entityType;  // Order, Product, Inventory, User...

    @Column(name = "entity_id", length = 100, nullable = false)
    private String entityId;

    @Column(name = "details", columnDefinition = "TEXT")
    private String detailsJson;  // JSON string

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
