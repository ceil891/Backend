package com.smartretail.backend.controller;

import com.smartretail.backend.dto.inventory.InventoryResponse;
import com.smartretail.backend.dto.inventory.InventoryTransactionRequest;
import com.smartretail.backend.dto.inventory.InventoryTransactionResponse;
import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAllInventory(
            @RequestParam(required = false) Integer storeId,
            @RequestParam(required = false) Integer productId) {
        try {
            List<InventoryResponse> inventories = inventoryService.getAllInventory(storeId, productId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách tồn kho thành công", inventories));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách tồn kho: " + e.getMessage(), null));
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getLowStockItems() {
        try {
            List<InventoryResponse> items = inventoryService.getLowStockItems();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách sản phẩm sắp hết hàng thành công", items));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryById(@PathVariable Integer id) {
        try {
            return inventoryService.getInventoryById(id)
                    .map(inv -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy tồn kho thành công", inv)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, "Tồn kho không tồn tại", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy tồn kho: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateInventory(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock) {
        try {
            InventoryResponse inventory = inventoryService.updateInventory(id, minStock, maxStock);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật tồn kho thành công", inventory));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi cập nhật tồn kho: " + e.getMessage(), null));
        }
    }

    @PostMapping("/transaction")
    public ResponseEntity<ApiResponse<InventoryTransactionResponse>> createTransaction(
            @RequestBody InventoryTransactionRequest request) {
        try {
            if (request.getStoreId() == null || request.getProductId() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "StoreId và ProductId là bắt buộc", null));
            }
            if (request.getType() == null || request.getQuantity() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Type và Quantity là bắt buộc", null));
            }

            InventoryTransactionResponse transaction = inventoryService.createTransaction(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tạo giao dịch kho thành công", transaction));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi tạo giao dịch: " + e.getMessage(), null));
        }
    }
}
