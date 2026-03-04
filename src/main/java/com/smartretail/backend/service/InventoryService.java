package com.smartretail.backend.service;

import com.smartretail.backend.dto.inventory.InventoryResponse;
import com.smartretail.backend.dto.inventory.InventoryTransactionRequest;
import com.smartretail.backend.dto.inventory.InventoryTransactionResponse;
import com.smartretail.backend.entity.CuaHang;
import com.smartretail.backend.entity.Inventory;
import com.smartretail.backend.entity.InventoryTransaction;
import com.smartretail.backend.entity.SanPham;
import com.smartretail.backend.repository.CuaHangRepository;
import com.smartretail.backend.repository.InventoryRepository;
import com.smartretail.backend.repository.InventoryTransactionRepository;
import com.smartretail.backend.repository.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final CuaHangRepository cuaHangRepository;
    private final SanPhamRepository sanPhamRepository;

    public List<InventoryResponse> getAllInventory(Integer storeId, Integer productId) {
        List<Inventory> inventories;
        if (storeId != null && productId != null) {
            Optional<Inventory> inv = inventoryRepository.findByStore_CuaHangIdAndProduct_SanPhamId(storeId, productId);
            inventories = inv.map(List::of).orElse(List.of());
        } else if (storeId != null) {
            inventories = inventoryRepository.findByStore_CuaHangId(storeId);
        } else if (productId != null) {
            inventories = inventoryRepository.findByProduct_SanPhamId(productId);
        } else {
            inventories = inventoryRepository.findAll();
        }

        return inventories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<InventoryResponse> getInventoryById(Integer id) {
        return inventoryRepository.findById(id)
                .map(this::toResponse);
    }

    public InventoryResponse updateInventory(Integer id, Integer minStock, Integer maxStock) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory không tồn tại"));

        if (minStock != null) {
            inventory.setMinStock(minStock);
        }
        if (maxStock != null) {
            inventory.setMaxStock(maxStock);
        }
        inventory.setLastUpdated(LocalDateTime.now());

        Inventory saved = inventoryRepository.save(inventory);
        return toResponse(saved);
    }

    @Transactional
    public InventoryTransactionResponse createTransaction(InventoryTransactionRequest request) {
        CuaHang store = cuaHangRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("Cửa hàng không tồn tại"));
        SanPham product = sanPhamRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Lấy hoặc tạo inventory
        Inventory inventory = inventoryRepository
                .findByStore_CuaHangIdAndProduct_SanPhamId(request.getStoreId(), request.getProductId())
                .orElseGet(() -> {
                    Inventory newInv = new Inventory();
                    newInv.setStore(store);
                    newInv.setProduct(product);
                    newInv.setQuantity(0);
                    newInv.setMinStock(0);
                    newInv.setLastUpdated(LocalDateTime.now());
                    return inventoryRepository.save(newInv);
                });

        // Tạo transaction
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setStore(store);
        transaction.setProduct(product);
        transaction.setType(request.getType());
        transaction.setQuantity(request.getQuantity());
        transaction.setReason(request.getReason());
        transaction.setCreatedBy(getCurrentUserId());
        transaction.setCreatedAt(LocalDateTime.now());

        // Xử lý điều chuyển
        if ("TRANSFER".equals(request.getType())) {
            if (request.getFromStoreId() != null) {
                CuaHang fromStore = cuaHangRepository.findById(request.getFromStoreId())
                        .orElseThrow(() -> new RuntimeException("Cửa hàng nguồn không tồn tại"));
                transaction.setFromStore(fromStore);
            }
            if (request.getToStoreId() != null) {
                CuaHang toStore = cuaHangRepository.findById(request.getToStoreId())
                        .orElseThrow(() -> new RuntimeException("Cửa hàng đích không tồn tại"));
                transaction.setToStore(toStore);
            }
        }

        InventoryTransaction saved = transactionRepository.save(transaction);

        // Cập nhật inventory
        int newQuantity = inventory.getQuantity() + request.getQuantity();
        if (newQuantity < 0) {
            throw new RuntimeException("Số lượng tồn kho không đủ");
        }
        inventory.setQuantity(newQuantity);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);

        return toTransactionResponse(saved);
    }

    public List<InventoryResponse> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Long getCurrentUserId() {
        // TODO: Get from authentication
        return 1L; // Mock
    }

    private InventoryResponse toResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getInventoryId())
                .storeId(inventory.getStore().getCuaHangId())
                .storeName(inventory.getStore().getTenCuaHang())
                .productId(inventory.getProduct().getSanPhamId())
                .productName(inventory.getProduct().getTenSanPham())
                .quantity(inventory.getQuantity())
                .minStock(inventory.getMinStock())
                .maxStock(inventory.getMaxStock())
                .lastUpdated(inventory.getLastUpdated())
                .build();
    }

    private InventoryTransactionResponse toTransactionResponse(InventoryTransaction transaction) {
        return InventoryTransactionResponse.builder()
                .id(transaction.getTransactionId())
                .storeId(transaction.getStore().getCuaHangId())
                .storeName(transaction.getStore().getTenCuaHang())
                .productId(transaction.getProduct().getSanPhamId())
                .productName(transaction.getProduct().getTenSanPham())
                .type(transaction.getType())
                .quantity(transaction.getQuantity())
                .fromStoreId(transaction.getFromStore() != null ? transaction.getFromStore().getCuaHangId() : null)
                .fromStoreName(transaction.getFromStore() != null ? transaction.getFromStore().getTenCuaHang() : null)
                .toStoreId(transaction.getToStore() != null ? transaction.getToStore().getCuaHangId() : null)
                .toStoreName(transaction.getToStore() != null ? transaction.getToStore().getTenCuaHang() : null)
                .reason(transaction.getReason())
                .createdBy(transaction.getCreatedBy())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
