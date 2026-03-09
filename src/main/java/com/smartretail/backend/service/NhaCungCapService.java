package com.smartretail.backend.service;

import com.smartretail.backend.entity.NhaCungCap;
import com.smartretail.backend.repository.NhaCungCapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;

    public List<NhaCungCap> getAll(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return nhaCungCapRepository.findAll();
        }
        return nhaCungCapRepository.search(keyword.trim());
    }

    public Optional<NhaCungCap> getById(Integer id) {
        return nhaCungCapRepository.findById(id);
    }

    public NhaCungCap create(NhaCungCap body) {
        body.setNhaCungCapId(null);
        if (body.getTrangThai() == null || body.getTrangThai().isBlank()) {
            body.setTrangThai("Đang giao dịch");
        }
        return nhaCungCapRepository.save(body);
    }

    public NhaCungCap update(Integer id, NhaCungCap body) {
        return nhaCungCapRepository.findById(id)
                .map(existing -> {
                    body.setNhaCungCapId(existing.getNhaCungCapId());
                    if (body.getTrangThai() == null || body.getTrangThai().isBlank()) {
                        body.setTrangThai(existing.getTrangThai());
                    }
                    return nhaCungCapRepository.save(body);
                })
                .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại"));
    }

    public void delete(Integer id) {
        nhaCungCapRepository.deleteById(id);
    }
}

