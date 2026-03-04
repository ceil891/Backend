package com.smartretail.backend.repository;

import com.smartretail.backend.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Integer> {

    List<InventoryTransaction> findByStore_CuaHangId(Integer storeId);

    List<InventoryTransaction> findByProduct_SanPhamId(Integer productId);
}
