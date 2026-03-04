package com.smartretail.backend.dto.notification;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long userId;
    private String type;      // INFO, WARNING, ERROR, SUCCESS
    private String title;
    private String message;
    private String link;
}
