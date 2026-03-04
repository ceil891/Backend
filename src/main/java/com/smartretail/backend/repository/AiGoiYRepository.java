package com.smartretail.backend.repository;

import com.smartretail.backend.entity.AiGoiY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiGoiYRepository extends JpaRepository<AiGoiY, Integer> {

    @Query("SELECT a FROM AiGoiY a WHERE a.agent.agentId = :agentId")
    List<AiGoiY> findByAgentId(@Param("agentId") Integer agentId);

    @Query("SELECT a FROM AiGoiY a WHERE a.khachHang.khachHangId = :khachHangId")
    List<AiGoiY> findByKhachHangId(@Param("khachHangId") Integer khachHangId);

    @Query("SELECT a FROM AiGoiY a WHERE a.bienThe.bienTheId = :bienTheId")
    List<AiGoiY> findByBienTheId(@Param("bienTheId") Integer bienTheId);

    @Query("SELECT a FROM AiGoiY a WHERE a.khachHang.khachHangId = :khachHangId ORDER BY a.diemSo DESC")
    List<AiGoiY> findByKhachHangOrderByDiemSoDesc(@Param("khachHangId") Integer khachHangId);

    @Query("SELECT a FROM AiGoiY a WHERE a.agent.agentId = :agentId AND a.khachHang.khachHangId = :khachHangId ORDER BY a.diemSo DESC LIMIT 10")
    List<AiGoiY> findTopRecommendationsByAgentAndCustomer(@Param("agentId") Integer agentId,
                                                          @Param("khachHangId") Integer khachHangId);

    @Query("SELECT AVG(a.diemSo) FROM AiGoiY a WHERE a.bienThe.bienTheId = :bienTheId")
    Double getAverageScoreForProductVariant(@Param("bienTheId") Integer bienTheId);
}