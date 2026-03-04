package com.smartretail.backend.repository;

import com.smartretail.backend.entity.WorkShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkShiftRepository extends JpaRepository<WorkShift, Integer> {

    List<WorkShift> findByStore_CuaHangId(Integer storeId);

    List<WorkShift> findByUserId(Long userId);

    @Query("SELECT w FROM WorkShift w WHERE w.store.cuaHangId = :storeId AND w.shiftDate = :shiftDate")
    List<WorkShift> findByStoreAndDate(@Param("storeId") Integer storeId, @Param("shiftDate") LocalDate shiftDate);
}
