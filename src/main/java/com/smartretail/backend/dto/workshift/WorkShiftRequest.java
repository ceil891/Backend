package com.smartretail.backend.dto.workshift;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class WorkShiftRequest {
    private Integer storeId;
    private Long userId;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String notes;
}
