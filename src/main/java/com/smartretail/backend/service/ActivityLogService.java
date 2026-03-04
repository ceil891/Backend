package com.smartretail.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartretail.backend.dto.activitylog.ActivityLogResponse;
import com.smartretail.backend.entity.ActivityLog;
import com.smartretail.backend.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Page<ActivityLogResponse> getActivityLogs(
            Long userId, String action, String entityType, String search,
            int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ActivityLog> logs = activityLogRepository.findByFilters(
                userId, action, entityType, search, pageable);
        return logs.map(this::toResponse);
    }

    @Transactional
    public void logActivity(Long userId, String action, String entityType, String entityId,
                            Map<String, Object> details, String ipAddress, String userAgent) {
        ActivityLog entity = new ActivityLog();
        entity.setUserId(userId);
        entity.setAction(action);
        entity.setEntityType(entityType);
        entity.setEntityId(entityId);
        entity.setIpAddress(ipAddress);
        entity.setUserAgent(userAgent);

        try {
            entity.setDetailsJson(objectMapper.writeValueAsString(details));
        } catch (JsonProcessingException e) {
            // dùng logger của lớp, không phải biến entity
            log.error("Error serializing details to JSON", e);
            entity.setDetailsJson("{}");
        }

        activityLogRepository.save(entity);
    }

    private ActivityLogResponse toResponse(ActivityLog entity) {
        Map<String, Object> details = new HashMap<>();
        try {
            if (entity.getDetailsJson() != null && !entity.getDetailsJson().isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> parsedDetails = objectMapper.readValue(entity.getDetailsJson(), Map.class);
                details = parsedDetails;
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing details JSON", e);
        }

        return ActivityLogResponse.builder()
                .id(entity.getLogId())
                .userId(entity.getUserId())
                .action(entity.getAction())
                .entityType(entity.getEntityType())
                .entityId(entity.getEntityId())
                .details(details)
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
