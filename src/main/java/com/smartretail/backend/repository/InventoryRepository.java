package com.smartretail.backend.repository;

import com.smartretail.backend.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findByStore_CuaHangIdAndProduct_SanPhamId(Integer storeId, Integer productId);

    List<Inventory> findByStore_CuaHangId(Integer storeId);

    List<Inventory> findByProduct_SanPhamId(Integer productId);

    @Query("SELECT i FROM Inventory i WHERE i.quantity <= i.minStock")
    List<Inventory> findLowStockItems();
}
