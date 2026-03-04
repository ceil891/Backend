package com.smartretail.backend.repository;

import com.smartretail.backend.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Integer> {

    Page<ActivityLog> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT a FROM ActivityLog a WHERE " +
           "(:userId IS NULL OR a.userId = :userId) AND " +
           "(:action IS NULL OR a.action LIKE %:action%) AND " +
           "(:entityType IS NULL OR a.entityType = :entityType) AND " +
           "(:search IS NULL OR a.action LIKE %:search% OR a.entityType LIKE %:search%)")
    Page<ActivityLog> findByFilters(
            @Param("userId") Long userId,
            @Param("action") String action,
            @Param("entityType") String entityType,
            @Param("search") String search,
            Pageable pageable);
}
