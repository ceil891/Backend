package com.smartretail.backend.service;

import com.smartretail.backend.dto.category.CategoryRequest;
import com.smartretail.backend.dto.category.CategoryResponse;
import com.smartretail.backend.entity.DanhMuc;
import com.smartretail.backend.repository.DanhMucRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DanhMucService {

    private final DanhMucRepository danhMucRepository;

    public List<CategoryResponse> getAllCategories(Integer parentId) {
        List<DanhMuc> categories;
        if (parentId != null) {
            categories = danhMucRepository.findByDanhMucCha_DanhMucId(parentId);
        } else {
            categories = danhMucRepository.findByDanhMucChaIsNull();
        }
        return categories.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<CategoryResponse> getCategoryById(Integer id) {
        return danhMucRepository.findById(id)
                .map(this::toResponse);
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setTenDanhMuc(request.getName());
        
        if (request.getParentId() != null) {
            DanhMuc parent = danhMucRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Danh mục cha không tồn tại"));
            danhMuc.setDanhMucCha(parent);
        }

        DanhMuc saved = danhMucRepository.save(danhMuc);
        return toResponse(saved);
    }

    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        danhMuc.setTenDanhMuc(request.getName());

        if (request.getParentId() != null && !request.getParentId().equals(
                danhMuc.getDanhMucCha() != null ? danhMuc.getDanhMucCha().getDanhMucId() : null)) {
            DanhMuc parent = danhMucRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Danh mục cha không tồn tại"));
            danhMuc.setDanhMucCha(parent);
        } else if (request.getParentId() == null) {
            danhMuc.setDanhMucCha(null);
        }

        DanhMuc saved = danhMucRepository.save(danhMuc);
        return toResponse(saved);
    }

    public void deleteCategory(Integer id) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        // Kiểm tra xem có danh mục con không
        if (!danhMuc.getDanhMucCons().isEmpty()) {
            throw new RuntimeException("Không thể xóa danh mục có danh mục con");
        }

        // Kiểm tra xem có sản phẩm nào thuộc danh mục này không
        if (!danhMuc.getSanPhams().isEmpty()) {
            throw new RuntimeException("Không thể xóa danh mục có sản phẩm");
        }

        danhMucRepository.delete(danhMuc);
    }

    private CategoryResponse toResponse(DanhMuc danhMuc) {
        return CategoryResponse.builder()
                .id(danhMuc.getDanhMucId())
                .name(danhMuc.getTenDanhMuc())
                .description(null)  // Entity chưa có field này
                .parentId(danhMuc.getDanhMucCha() != null ? danhMuc.getDanhMucCha().getDanhMucId() : null)
                .image(null)  // Entity chưa có field này
                .isActive(true)  // Entity chưa có field này, mặc định true
                .createdAt(LocalDateTime.now())  // Entity chưa có field này
                .updatedAt(LocalDateTime.now())  // Entity chưa có field này
                .build();
    }
}
