package com.smartretail.backend.repository;

import com.smartretail.backend.entity.AiDuDoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiDuDoanRepository extends JpaRepository<AiDuDoan, Integer> {

    @Query("SELECT a FROM AiDuDoan a WHERE a.agent.agentId = :agentId")
    List<AiDuDoan> findByAgentId(@Param("agentId") Integer agentId);

    @Query("SELECT a FROM AiDuDoan a WHERE a.cuaHang.cuaHangId = :cuaHangId")
    List<AiDuDoan> findByCuaHangId(@Param("cuaHangId") Integer cuaHangId);

    @Query("SELECT a FROM AiDuDoan a WHERE a.bienThe.bienTheId = :bienTheId")
    List<AiDuDoan> findByBienTheId(@Param("bienTheId") Integer bienTheId);

    List<AiDuDoan> findByLoaiDuDoan(String loaiDuDoan);

    @Query("SELECT a FROM AiDuDoan a WHERE a.cuaHang.cuaHangId = :cuaHangId AND a.loaiDuDoan = :loaiDuDoan ORDER BY a.giaTri DESC")
    List<AiDuDoan> findByCuaHangAndLoaiDuDoanOrderByGiaTriDesc(@Param("cuaHangId") Integer cuaHangId,
                                                              @Param("loaiDuDoan") String loaiDuDoan);

    @Query("SELECT a FROM AiDuDoan a WHERE a.bienThe.bienTheId = :bienTheId AND a.khoangThoiGian = :khoangThoiGian")
    List<AiDuDoan> findByBienTheAndKhoangThoiGian(@Param("bienTheId") Integer bienTheId,
                                                  @Param("khoangThoiGian") String khoangThoiGian);

    @Query("SELECT SUM(a.giaTri) FROM AiDuDoan a WHERE a.cuaHang.cuaHangId = :cuaHangId AND a.loaiDuDoan = 'sales' AND a.khoangThoiGian = :period")
    Double getTotalPredictedSalesForStore(@Param("cuaHangId") Integer cuaHangId, @Param("period") String period);
}