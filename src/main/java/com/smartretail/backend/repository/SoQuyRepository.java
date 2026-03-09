package com.smartretail.backend.repository;

import com.smartretail.backend.entity.SoQuy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SoQuyRepository extends JpaRepository<SoQuy, Integer> {

    @Query("""
            SELECT s
            FROM SoQuy s
            WHERE (:fromDate IS NULL OR s.thoiGian >= :fromDate)
              AND (:toDate IS NULL OR s.thoiGian <= :toDate)
              AND (
                    :keyword IS NULL OR :keyword = '' OR
                    LOWER(COALESCE(s.thamChieu, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(COALESCE(s.doiTuong, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
            ORDER BY s.thoiGian DESC
            """)
    List<SoQuy> search(@Param("fromDate") Timestamp fromDate,
                       @Param("toDate") Timestamp toDate,
                       @Param("keyword") String keyword);
}

