package com.smartretail.backend.service;

import com.smartretail.backend.entity.DonVi;
import com.smartretail.backend.repository.DonViRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DonViService {

    private final DonViRepository donViRepository;

    public List<DonVi> getAllDonVi() {
        return donViRepository.findAll();
    }

    public Optional<DonVi> getDonViById(Integer id) {
        return donViRepository.findById(id);
    }

    public Optional<DonVi> getDonViByTen(String tenDonVi) {
        return donViRepository.findByTenDonVi(tenDonVi);
    }

    public DonVi saveDonVi(DonVi donVi) {
        return donViRepository.save(donVi);
    }

    public DonVi updateDonVi(DonVi donVi) {
        return donViRepository.save(donVi);
    }

    public void deleteDonVi(Integer id) {
        donViRepository.deleteById(id);
    }
}
