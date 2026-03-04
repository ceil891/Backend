package com.smartretail.backend.controller;

import com.smartretail.backend.dto.category.CategoryRequest;
import com.smartretail.backend.dto.category.CategoryResponse;
import com.smartretail.backend.dto.response.ApiResponse;
import com.smartretail.backend.service.DanhMucService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/danh-muc")
@RequiredArgsConstructor
public class DanhMucController {

    private final DanhMucService danhMucService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(
            @RequestParam(required = false) Integer parentId) {
        try {
            List<CategoryResponse> categories = danhMucService.getAllCategories(parentId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách danh mục thành công", categories));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách danh mục: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Integer id) {
        try {
            return danhMucService.getCategoryById(id)
                    .map(category -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh mục thành công", category)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse<>(false, "Danh mục không tồn tại", null)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh mục: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest request) {
        try {
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Tên danh mục là bắt buộc", null));
            }

            CategoryResponse category = danhMucService.createCategory(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Tạo danh mục thành công", category));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi tạo danh mục: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Integer id,
            @RequestBody CategoryRequest request) {
        try {
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Tên danh mục là bắt buộc", null));
            }

            CategoryResponse category = danhMucService.updateCategory(id, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật danh mục thành công", category));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi cập nhật danh mục: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Integer id) {
        try {
            danhMucService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Xóa danh mục thành công", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi xóa danh mục: " + e.getMessage(), null));
        }
    }
}
