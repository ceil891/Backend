package com.smartretail.backend.controller;

import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.dto.store.StoreRequest;
import com.smartretail.backend.dto.store.StoreResponse;
import com.smartretail.backend.service.CuaHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cua-hang")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CuaHangController {

    private final CuaHangService cuaHangService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getAllStores() {
        try {
            List<StoreResponse> stores = cuaHangService.getAllStores();
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách cửa hàng thành công", stores));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách cửa hàng: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreById(@PathVariable Integer id) {
        try {
            return cuaHangService.getStoreById(id)
                    .map(store -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy cửa hàng thành công", store)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, "Cửa hàng không tồn tại", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy cửa hàng: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(@RequestBody StoreRequest request) {
        try {
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Tên cửa hàng là bắt buộc", null));
            }
            if (request.getAddress() == null || request.getAddress().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Địa chỉ là bắt buộc", null));
            }
            if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Số điện thoại là bắt buộc", null));
            }

            StoreResponse store = cuaHangService.createStore(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tạo cửa hàng thành công", store));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi tạo cửa hàng: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreResponse>> updateStore(
            @PathVariable Integer id,
            @RequestBody StoreRequest request) {
        try {
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Tên cửa hàng là bắt buộc", null));
            }

            StoreResponse store = cuaHangService.updateStore(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật cửa hàng thành công", store));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi cập nhật cửa hàng: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStore(@PathVariable Integer id) {
        try {
            cuaHangService.deleteStore(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Xóa cửa hàng thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi xóa cửa hàng: " + e.getMessage(), null));
        }
    }
}
