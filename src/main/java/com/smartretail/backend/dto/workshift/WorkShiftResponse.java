package com.smartretail.backend.dto.workshift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkShiftResponse {
    private Integer id;
    private Integer storeId;
    private String storeName;
    private Long userId;
    private String userName;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String notes;
    private LocalDateTime createdAt;
}
