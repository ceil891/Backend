package com.smartretail.backend.repository;

import com.smartretail.backend.entity.DonVi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonViRepository extends JpaRepository<DonVi, Integer> {
    Optional<DonVi> findByTenDonVi(String tenDonVi);
}
