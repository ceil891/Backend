package com.smartretail.backend.dto.settings;

import lombok.Data;

@Data
public class NotificationSettingsRequest {
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean lowStockAlerts;
    private Boolean aiRecommendations;
}
