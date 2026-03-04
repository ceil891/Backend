package com.smartretail.backend.service;

import com.smartretail.backend.dto.voucher.VoucherRequest;
import com.smartretail.backend.dto.voucher.VoucherResponse;
import com.smartretail.backend.dto.voucher.VoucherValidationRequest;
import com.smartretail.backend.dto.voucher.VoucherValidationResponse;
import com.smartretail.backend.entity.Voucher;
import com.smartretail.backend.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public List<VoucherResponse> getAllVouchers() {
        return voucherRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<VoucherResponse> getActiveVouchers() {
        return voucherRepository.findByIsActiveTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<VoucherResponse> getVoucherById(Integer id) {
        return voucherRepository.findById(id)
                .map(this::toResponse);
    }

    public Optional<VoucherResponse> getVoucherByCode(String code) {
        return voucherRepository.findByCode(code)
                .map(this::toResponse);
    }

    public VoucherResponse createVoucher(VoucherRequest request) {
        // Kiểm tra code đã tồn tại chưa
        if (voucherRepository.findByCode(request.getCode()).isPresent()) {
            throw new RuntimeException("Mã voucher đã tồn tại");
        }

        Voucher voucher = new Voucher();
        voucher.setCode(request.getCode().toUpperCase());
        voucher.setName(request.getName());
        voucher.setDescription(request.getDescription());
        voucher.setDiscountType(request.getDiscountType());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinPurchase(request.getMinPurchase());
        voucher.setMaxDiscount(request.getMaxDiscount());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        voucher.setMaxUsage(request.getMaxUsage());
        voucher.setMaxUsagePerUser(request.getMaxUsagePerUser());
        voucher.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        voucher.setCurrentUsage(0);
        voucher.setCreatedAt(LocalDateTime.now());
        voucher.setUpdatedAt(LocalDateTime.now());

        Voucher saved = voucherRepository.save(voucher);
        return toResponse(saved);
    }

    public VoucherResponse updateVoucher(Integer id, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));

        // Kiểm tra code nếu có thay đổi
        if (!voucher.getCode().equals(request.getCode().toUpperCase())) {
            if (voucherRepository.findByCode(request.getCode()).isPresent()) {
                throw new RuntimeException("Mã voucher đã tồn tại");
            }
        }

        voucher.setCode(request.getCode().toUpperCase());
        voucher.setName(request.getName());
        voucher.setDescription(request.getDescription());
        voucher.setDiscountType(request.getDiscountType());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinPurchase(request.getMinPurchase());
        voucher.setMaxDiscount(request.getMaxDiscount());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        voucher.setMaxUsage(request.getMaxUsage());
        voucher.setMaxUsagePerUser(request.getMaxUsagePerUser());
        voucher.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        voucher.setUpdatedAt(LocalDateTime.now());

        Voucher saved = voucherRepository.save(voucher);
        return toResponse(saved);
    }

    public void deleteVoucher(Integer id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
        voucherRepository.delete(voucher);
    }

    public VoucherValidationResponse validateVoucher(VoucherValidationRequest request) {
        Optional<Voucher> voucherOpt = voucherRepository.findByCode(request.getCode().toUpperCase());

        if (voucherOpt.isEmpty()) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Mã voucher không tồn tại")
                    .discountAmount(0.0)
                    .build();
        }

        Voucher voucher = voucherOpt.get();
        LocalDate today = LocalDate.now();

        // Kiểm tra trạng thái
        if (!voucher.getIsActive()) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Voucher đã bị vô hiệu hóa")
                    .discountAmount(0.0)
                    .build();
        }

        // Kiểm tra thời gian
        if (today.isBefore(voucher.getStartDate()) || today.isAfter(voucher.getEndDate())) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Voucher đã hết hạn hoặc chưa đến thời gian sử dụng")
                    .discountAmount(0.0)
                    .build();
        }

        // Kiểm tra số lần sử dụng
        if (voucher.getMaxUsage() != null && voucher.getCurrentUsage() >= voucher.getMaxUsage()) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Voucher đã hết lượt sử dụng")
                    .discountAmount(0.0)
                    .build();
        }

        // Kiểm tra giá trị đơn hàng tối thiểu
        if (voucher.getMinPurchase() != null && request.getOrderAmount() < voucher.getMinPurchase()) {
            return VoucherValidationResponse.builder()
                    .isValid(false)
                    .message("Đơn hàng phải có giá trị tối thiểu " + voucher.getMinPurchase())
                    .discountAmount(0.0)
                    .build();
        }

        // Tính số tiền giảm
        double discountAmount = 0.0;
        if ("PERCENTAGE".equals(voucher.getDiscountType())) {
            discountAmount = request.getOrderAmount() * voucher.getDiscountValue() / 100.0;
            if (voucher.getMaxDiscount() != null && discountAmount > voucher.getMaxDiscount()) {
                discountAmount = voucher.getMaxDiscount();
            }
        } else if ("FIXED".equals(voucher.getDiscountType())) {
            discountAmount = voucher.getDiscountValue();
        }

        return VoucherValidationResponse.builder()
                .isValid(true)
                .message("Voucher hợp lệ")
                .discountAmount(discountAmount)
                .voucher(toResponse(voucher))
                .build();
    }

    @Transactional
    public void useVoucher(String code) {
        Voucher voucher = voucherRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
        voucher.setCurrentUsage(voucher.getCurrentUsage() + 1);
        voucherRepository.save(voucher);
    }

    private VoucherResponse toResponse(Voucher voucher) {
        return VoucherResponse.builder()
                .id(voucher.getVoucherId())
                .code(voucher.getCode())
                .name(voucher.getName())
                .description(voucher.getDescription())
                .discountType(voucher.getDiscountType())
                .discountValue(voucher.getDiscountValue())
                .minPurchase(voucher.getMinPurchase())
                .maxDiscount(voucher.getMaxDiscount())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .maxUsage(voucher.getMaxUsage())
                .maxUsagePerUser(voucher.getMaxUsagePerUser())
                .currentUsage(voucher.getCurrentUsage())
                .isActive(voucher.getIsActive())
                .createdAt(voucher.getCreatedAt())
                .updatedAt(voucher.getUpdatedAt())
                .build();
    }
}
