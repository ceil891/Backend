package com.smartretail.backend.service;

import com.smartretail.backend.dto.workshift.WorkShiftRequest;
import com.smartretail.backend.dto.workshift.WorkShiftResponse;
import com.smartretail.backend.entity.CuaHang;
import com.smartretail.backend.entity.WorkShift;
import com.smartretail.backend.repository.CuaHangRepository;
import com.smartretail.backend.repository.WorkShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkShiftService {

    private final WorkShiftRepository workShiftRepository;
    private final CuaHangRepository cuaHangRepository;

    public List<WorkShiftResponse> getAllWorkShifts(Integer storeId, Long userId, LocalDate shiftDate) {
        List<WorkShift> shifts;
        if (storeId != null && shiftDate != null) {
            shifts = workShiftRepository.findByStoreAndDate(storeId, shiftDate);
        } else if (storeId != null) {
            shifts = workShiftRepository.findByStore_CuaHangId(storeId);
        } else if (userId != null) {
            shifts = workShiftRepository.findByUserId(userId);
        } else {
            shifts = workShiftRepository.findAll();
        }

        return shifts.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<WorkShiftResponse> getWorkShiftById(Integer id) {
        return workShiftRepository.findById(id)
                .map(this::toResponse);
    }

    public WorkShiftResponse createWorkShift(WorkShiftRequest request) {
        CuaHang store = cuaHangRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Cửa hàng không tồn tại"));

        WorkShift shift = new WorkShift();
        shift.setStore(store);
        shift.setUserId(request.getUserId());
        shift.setShiftDate(request.getShiftDate());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setNotes(request.getNotes());

        WorkShift saved = workShiftRepository.save(shift);
        return toResponse(saved);
    }

    public WorkShiftResponse updateWorkShift(Integer id, WorkShiftRequest request) {
        WorkShift shift = workShiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ca làm việc không tồn tại"));

        if (request.getStoreId() != null) {
            CuaHang store = cuaHangRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Cửa hàng không tồn tại"));
            shift.setStore(store);
        }
        if (request.getUserId() != null) {
            shift.setUserId(request.getUserId());
        }
        if (request.getShiftDate() != null) {
            shift.setShiftDate(request.getShiftDate());
        }
        if (request.getStartTime() != null) {
            shift.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            shift.setEndTime(request.getEndTime());
        }
        if (request.getNotes() != null) {
            shift.setNotes(request.getNotes());
        }

        WorkShift saved = workShiftRepository.save(shift);
        return toResponse(saved);
    }

    public void deleteWorkShift(Integer id) {
        WorkShift shift = workShiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ca làm việc không tồn tại"));
        workShiftRepository.delete(shift);
    }

    private WorkShiftResponse toResponse(WorkShift shift) {
        return WorkShiftResponse.builder()
                .id(shift.getShiftId())
                .storeId(shift.getStore().getCuaHangId())
                .storeName(shift.getStore().getTenCuaHang())
                .userId(shift.getUserId())
                .userName("User " + shift.getUserId())  // TODO: Get from User service
                .shiftDate(shift.getShiftDate())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .notes(shift.getNotes())
                .createdAt(shift.getCreatedAt())
                .build();
    }
}
