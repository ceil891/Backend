package com.smartretail.backend.service;

import com.smartretail.backend.dto.store.StoreRequest;
import com.smartretail.backend.dto.store.StoreResponse;
import com.smartretail.backend.entity.CuaHang;
import com.smartretail.backend.entity.KhuVuc;
import com.smartretail.backend.repository.CuaHangRepository;
import com.smartretail.backend.repository.KhuVucRepository;
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
public class CuaHangService {

    private final CuaHangRepository cuaHangRepository;
    private final KhuVucRepository khuVucRepository;

    public List<StoreResponse> getAllStores() {
        return cuaHangRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<StoreResponse> getStoreById(Integer id) {
        return cuaHangRepository.findById(id)
                .map(this::toResponse);
    }

    public StoreResponse createStore(StoreRequest request) {
        CuaHang cuaHang = new CuaHang();
        
        // Lấy khu vực mặc định (hoặc tạo mới nếu chưa có)
        KhuVuc khuVuc = khuVucRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    KhuVuc newKhuVuc = new KhuVuc();
                    newKhuVuc.setTenKhuVuc("Khu vực mặc định");
                    return khuVucRepository.save(newKhuVuc);
                });
        
        cuaHang.setKhuVuc(khuVuc);
        cuaHang.setTenCuaHang(request.getName());
        cuaHang.setDiaChi(request.getAddress());
        cuaHang.setDienThoai(request.getPhone());
        cuaHang.setTrangThai(request.getIsActive() != null && request.getIsActive() ? "ACTIVE" : "INACTIVE");

        CuaHang saved = cuaHangRepository.save(cuaHang);
        return toResponse(saved);
    }

    public StoreResponse updateStore(Integer id, StoreRequest request) {
        CuaHang cuaHang = cuaHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cửa hàng không tồn tại"));

        cuaHang.setTenCuaHang(request.getName());
        cuaHang.setDiaChi(request.getAddress());
        cuaHang.setDienThoai(request.getPhone());
        cuaHang.setTrangThai(request.getIsActive() != null && request.getIsActive() ? "ACTIVE" : "INACTIVE");

        CuaHang saved = cuaHangRepository.save(cuaHang);
        return toResponse(saved);
    }

    public void deleteStore(Integer id) {
        CuaHang cuaHang = cuaHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cửa hàng không tồn tại"));

        // Kiểm tra xem có hóa đơn nào không
        if (!cuaHang.getHoaDons().isEmpty()) {
            throw new RuntimeException("Không thể xóa cửa hàng có hóa đơn");
        }

        cuaHangRepository.delete(cuaHang);
    }

    private StoreResponse toResponse(CuaHang cuaHang) {
        return StoreResponse.builder()
                .id(cuaHang.getCuaHangId())
                .code("CH" + String.format("%03d", cuaHang.getCuaHangId()))  // Generate code từ ID
                .name(cuaHang.getTenCuaHang())
                .address(cuaHang.getDiaChi())
                .phone(cuaHang.getDienThoai())
                .email(null)  // Entity chưa có field này
                .managerId(null)  // Cần map từ NhanVien nếu có
                .isActive("ACTIVE".equals(cuaHang.getTrangThai()))
                .createdAt(LocalDateTime.now())  // Entity chưa có field này
                .updatedAt(LocalDateTime.now())  // Entity chưa có field này
                .build();
    }
}
