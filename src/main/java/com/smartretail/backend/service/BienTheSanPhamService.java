package com.smartretail.backend.service;

import com.smartretail.backend.entity.BienTheSanPham;
import com.smartretail.backend.repository.BienTheSanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BienTheSanPhamService {

    private final BienTheSanPhamRepository bienTheSanPhamRepository;
    private final GoogleSheetsService googleSheetsService;

    public List<BienTheSanPham> getAllBienThe() {
        return bienTheSanPhamRepository.findAll();
    }

    public Optional<BienTheSanPham> getBienTheById(Integer id) {
        return bienTheSanPhamRepository.findById(id);
    }

    public List<BienTheSanPham> getBienTheBySanPhamId(Integer sanPhamId) {
        return bienTheSanPhamRepository.findBySanPham_SanPhamId(sanPhamId);
    }

    public Optional<BienTheSanPham> getBienTheByMaSku(String maSku) {
        return bienTheSanPhamRepository.findByMaSku(maSku);
    }

    public Optional<BienTheSanPham> getBienTheByMaVach(String maVach) {
        return bienTheSanPhamRepository.findByMaVach(maVach);
    }

    public List<BienTheSanPham> searchBienThe(String keyword) {
        return bienTheSanPhamRepository.searchByKeyword(keyword);
    }

    public BienTheSanPham saveBienThe(BienTheSanPham bienThe) {
        BienTheSanPham saved = bienTheSanPhamRepository.save(bienThe);
        googleSheetsService.appendRow("variants", List.of(
                "CREATE_VARIANT",
                saved.getBienTheId(),
                saved.getSanPham() != null ? saved.getSanPham().getSanPhamId() : null,
                saved.getMaSku(),
                saved.getTenBienThe(),
                saved.getGiaBan(),
                saved.getGiaNhap(),
                saved.getHoatDong()
        ));
        return saved;
    }

    public BienTheSanPham updateBienThe(BienTheSanPham bienThe) {
        return bienTheSanPhamRepository.save(bienThe);
    }

    public void deleteBienThe(Integer id) {
        bienTheSanPhamRepository.deleteById(id);
    }

    public List<BienTheSanPham> getBienTheBySanPhamOrderByGiaBan(Integer sanPhamId) {
        return bienTheSanPhamRepository.findBySanPhamIdOrderByGiaBanAsc(sanPhamId);
    }
}