package com.smartretail.backend.repository;

import com.smartretail.backend.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    Optional<Voucher> findByCode(String code);

    List<Voucher> findByIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate start, LocalDate end);

    List<Voucher> findByIsActiveTrue();
}
