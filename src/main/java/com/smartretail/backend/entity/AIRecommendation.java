package com.smartretail.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "ai_recommendation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Integer recommendationId;

    @Column(name = "type", length = 50, nullable = false)
    private String type;  // LOW_STOCK, DEMAND_PREDICTION, SLOW_MOVING, REVENUE_ANOMALY, TRANSFER_SUGGESTION

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "priority", length = 20, nullable = false)
    private String priority;  // LOW, MEDIUM, HIGH, URGENT

    @Column(name = "data", columnDefinition = "TEXT")
    private String dataJson;  // Lưu JSON string

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "is_resolved", nullable = false)
    private Boolean isResolved = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
