package com.smartretail.backend.repository;

import com.smartretail.backend.entity.AIRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIRecommendationRepository extends JpaRepository<AIRecommendation, Integer> {

    List<AIRecommendation> findByStoreId(Integer storeId);

    List<AIRecommendation> findByType(String type);

    List<AIRecommendation> findByPriority(String priority);

    List<AIRecommendation> findByIsResolvedFalse();

    List<AIRecommendation> findByIsReadFalse();

    @Query("SELECT a FROM AIRecommendation a WHERE " +
           "(:storeId IS NULL OR a.storeId = :storeId) AND " +
           "(:type IS NULL OR a.type = :type) AND " +
           "(:priority IS NULL OR a.priority = :priority) AND " +
           "(:isResolved IS NULL OR a.isResolved = :isResolved)")
    List<AIRecommendation> findByFilters(
            @Param("storeId") Integer storeId,
            @Param("type") String type,
            @Param("priority") String priority,
            @Param("isResolved") Boolean isResolved);
}
