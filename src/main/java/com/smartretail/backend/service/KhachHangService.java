package com.smartretail.backend.service;

import com.smartretail.backend.entity.KhachHang;
import com.smartretail.backend.repository.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class KhachHangService {

    private final KhachHangRepository khachHangRepository;

    public List<KhachHang> getAllKhachHang() {
        return khachHangRepository.findAll();
    }

    public Optional<KhachHang> getKhachHangById(Integer id) {
        return khachHangRepository.findById(id);
    }

    public Optional<KhachHang> getKhachHangByDienThoai(String dienThoai) {
        return khachHangRepository.findByDienThoai(dienThoai);
    }

    public Optional<KhachHang> getKhachHangByEmail(String email) {
        return khachHangRepository.findByEmail(email);
    }

    public List<KhachHang> getKhachHangByTrangThai(String trangThai) {
        return khachHangRepository.findByTrangThai(trangThai);
    }

    public List<KhachHang> searchKhachHang(String keyword) {
        return khachHangRepository.searchByKeyword(keyword);
    }

    public Long countNewCustomersInMonth(int month, int year) {
        return khachHangRepository.countNewCustomersInMonth(month, year);
    }

    public KhachHang saveKhachHang(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    public KhachHang updateKhachHang(KhachHang khachHang) {
        return khachHangRepository.save(khachHang);
    }

    public void deleteKhachHang(Integer id) {
        khachHangRepository.deleteById(id);
    }

    public void updateTrangThai(Integer id, String trangThai) {
        khachHangRepository.findById(id).ifPresent(khachHang -> {
            khachHang.setTrangThai(trangThai);
            khachHangRepository.save(khachHang);
        });
    }
}