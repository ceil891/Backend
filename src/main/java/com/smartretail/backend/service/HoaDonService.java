package com.smartretail.backend.service;

import com.smartretail.backend.dto.ChiTietHoaDonDTO;
import com.smartretail.backend.dto.HoaDonDTO;
import com.smartretail.backend.dto.pos.CreateOrderRequest;
import com.smartretail.backend.dto.pos.OrderSummaryDTO;
import com.smartretail.backend.entity.*;
import com.smartretail.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // ✅ Bắt buộc import thêm cái này

@Service
@RequiredArgsConstructor
@Transactional
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final BienTheSanPhamRepository bienTheSanPhamRepository;
    private final CuaHangRepository cuaHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final UserRepository userRepository;
    private final SanPhamRepository sanPhamRepository;
    private final GoogleSheetsService googleSheetsService;

    // ✅ BƯỚC ĐỘT PHÁ Ở ĐÂY: Trả về HoaDonDTO chứa sẵn danh sách Sản phẩm
   public List<HoaDonDTO> getAllHoaDonDTOs() {
    // Lấy tất cả hóa đơn
    List<HoaDon> hoaDons = hoaDonRepository.findAll();
    
    return hoaDons.stream().map(hoaDon -> {
        // Ép Spring Boot chạy câu query lấy chi tiết bằng Repository
        List<ChiTietHoaDon> chiTietList = chiTietHoaDonRepository.findByHoaDon_HoaDonId(hoaDon.getHoaDonId());
        // Gói vào DTO
        return mapToHoaDonDTO(hoaDon, chiTietList);
    }).collect(Collectors.toList());
}
    // Các hàm get khác tôi khuyên bạn sau này cũng nên đổi sang DTO
    public Optional<HoaDon> getHoaDonById(Integer id) {
        return hoaDonRepository.findById(id);
    }

    public List<HoaDon> getHoaDonByCuaHang(Integer cuaHangId) {
        return hoaDonRepository.findByCuaHang_CuaHangId(cuaHangId);
    }

    public List<HoaDon> getHoaDonByKhachHang(Integer khachHangId) {
        return hoaDonRepository.findByKhachHang_KhachHangId(khachHangId);
    }

    public List<HoaDon> getHoaDonByNhanVien(Integer nhanVienId) {
        return hoaDonRepository.findByNhanVien_NhanVienId(nhanVienId);
    }

    public List<HoaDon> getHoaDonByTrangThai(String trangThai) {
        return hoaDonRepository.findByTrangThai(trangThai);
    }

    public List<HoaDon> getHoaDonByDateRange(Timestamp startDate, Timestamp endDate) {
        return hoaDonRepository.findByDateRange(startDate, endDate);
    }

    public List<HoaDon> getHoaDonByCuaHangAndDateRange(Integer cuaHangId, Timestamp startDate, Timestamp endDate) {
        return hoaDonRepository.findByCuaHangAndDateRange(cuaHangId, startDate, endDate);
    }

    public Double getTotalRevenueInPeriod(Timestamp startDate, Timestamp endDate) {
        return hoaDonRepository.getTotalRevenueInPeriod(startDate, endDate);
    }

    public HoaDon saveHoaDon(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon updateHoaDon(HoaDon hoaDon) {
        return hoaDonRepository.save(hoaDon);
    }

    public void deleteHoaDon(Integer id) {
        hoaDonRepository.deleteById(id);
    }

    public void updateTrangThai(Integer id, String trangThai) {
        hoaDonRepository.findById(id).ifPresent(hoaDon -> {
            hoaDon.setTrangThai(trangThai);
            hoaDonRepository.save(hoaDon);
        });
    }

    public OrderSummaryDTO mapToOrderSummary(HoaDon hoaDon) {
        return new OrderSummaryDTO(
                hoaDon.getHoaDonId(),
                hoaDon.getMaHoaDon() != null ? hoaDon.getMaHoaDon() : ("HD" + hoaDon.getHoaDonId()),
                hoaDon.getCuaHang() != null ? hoaDon.getCuaHang().getCuaHangId() : null,
                hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getNhanVienId() : null,
                hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getHoTen() : null,
                hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getDienThoai() : null,
                hoaDon.getTamTinh(),
                hoaDon.getChietKhau(),
                hoaDon.getTongPhaiThanhToan(),
                hoaDon.getPhuongThucThanhToan() != null ? hoaDon.getPhuongThucThanhToan() : "CASH",
                hoaDon.getTrangThai(),
                hoaDon.getNgayLap().toInstant().atOffset(java.time.ZoneOffset.UTC)
        );
    }

    public HoaDonDTO mapToHoaDonDTO(HoaDon hoaDon, List<ChiTietHoaDon> chiTietList) {
        HoaDonDTO dto = new HoaDonDTO();
        dto.setHoaDonId(hoaDon.getHoaDonId());
        dto.setMaHoaDon(hoaDon.getMaHoaDon());
        dto.setKenhBan(hoaDon.getKenhBan());
        if (hoaDon.getCuaHang() != null) {
            dto.setCuaHangId(hoaDon.getCuaHang().getCuaHangId());
            dto.setTenCuaHang(hoaDon.getCuaHang().getTenCuaHang());
        }
        if (hoaDon.getKhachHang() != null) {
            dto.setKhachHangId(hoaDon.getKhachHang().getKhachHangId());
            dto.setTenKhachHang(hoaDon.getKhachHang().getHoTen());
            dto.setDienThoaiKhachHang(hoaDon.getKhachHang().getDienThoai());
        }
        if (hoaDon.getNhanVien() != null) {
            dto.setNhanVienId(hoaDon.getNhanVien().getNhanVienId());
            dto.setTenNhanVien(hoaDon.getNhanVien().getHoTen());
        }
        dto.setNgayLap(hoaDon.getNgayLap());
        dto.setPhuongThucThanhToan(hoaDon.getPhuongThucThanhToan());
        dto.setGhiChu(hoaDon.getGhiChu());
        dto.setTamTinh(hoaDon.getTamTinh());
        dto.setTienThue(hoaDon.getTienThue());
        dto.setChietKhau(hoaDon.getChietKhau());
        dto.setPhiShip(hoaDon.getPhiShip());
        dto.setTongPhaiThanhToan(hoaDon.getTongPhaiThanhToan());
        dto.setTrangThai(hoaDon.getTrangThai());
        dto.setNgayHuy(hoaDon.getNgayHuy());
        dto.setLyDoHuy(hoaDon.getLyDoHuy());
        dto.setNguoiHuy(hoaDon.getNguoiHuy());

        List<ChiTietHoaDonDTO> chiTietDtos = chiTietList.stream().map(ct -> {
            ChiTietHoaDonDTO c = new ChiTietHoaDonDTO();
            c.setChiTietId(ct.getChiTietId());
            c.setHoaDonId(hoaDon.getHoaDonId());
            if (ct.getBienThe() != null) {
                c.setBienTheId(ct.getBienThe().getBienTheId());
                c.setTenBienThe(ct.getBienThe().getTenBienThe());
                c.setMaSku(ct.getBienThe().getMaSku());
                if (ct.getBienThe().getSanPham() != null) {
                    c.setTenSanPham(ct.getBienThe().getSanPham().getTenSanPham());
                }
            }
            c.setSoLuong(ct.getSoLuong());
            c.setDonGia(ct.getDonGia());
            c.setThanhTien(ct.getThanhTien());
            return c;
        }).toList();
        
        dto.setChiTietHoaDons(chiTietDtos); // ✅ Cực kỳ quan trọng, dòng này gắn sản phẩm vào
        return dto;
    }

    public List<HoaDonDTO> searchOrders(Integer storeId, String channel, String status, Timestamp fromDate, Timestamp toDate, String keyword) {
        List<HoaDon> hoaDons = hoaDonRepository.searchOrders(storeId, channel, status, fromDate, toDate, keyword);
        return hoaDons.stream()
                .map(h -> mapToHoaDonDTO(h, chiTietHoaDonRepository.findByHoaDon_HoaDonId(h.getHoaDonId())))
                .collect(Collectors.toList());
    }

    public HoaDon createOrderFromPos(CreateOrderRequest request) {
        // 1. Tìm cửa hàng
        CuaHang cuaHang = cuaHangRepository.findById(request.getCuaHangId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cửa hàng"));

        // 2. Tìm/Tạo Nhân viên
        NhanVien nhanVien = nhanVienRepository.findById(request.getNhanVienId())
                .orElseGet(() -> {
                    User user = userRepository.findById(Long.valueOf(request.getNhanVienId()))
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản: " + request.getNhanVienId()));
                    NhanVien mergeNv = new NhanVien();
                    mergeNv.setNhanVienId(request.getNhanVienId());
                    mergeNv.setHoTen(user.getFullName());
                    mergeNv.setEmail(user.getEmail());
                    mergeNv.setMatKhauHash(user.getPassword());
                    mergeNv.setTrangThai("ACTIVE");
                    mergeNv.setCuaHang(cuaHang);
                    return nhanVienRepository.save(mergeNv);
                });

        HoaDon hoaDon = new HoaDon();
        hoaDon.setCuaHang(cuaHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setNgayLap(new Timestamp(System.currentTimeMillis()));
        hoaDon.setTrangThai(request.getStatus() != null ? request.getStatus() : "COMPLETED");
        hoaDon.setKenhBan(request.getKenhBan() != null ? request.getKenhBan() : "RETAIL");
        hoaDon.setPhuongThucThanhToan(request.getPaymentMethod());
        hoaDon.setGhiChu(request.getNotes());

        // 3. Xử lý Chi tiết hóa đơn
        List<ChiTietHoaDon> chiTietList = request.getItems().stream().map(item -> {
            BienTheSanPham bienThe = bienTheSanPhamRepository.findById(item.getBienTheId())
                    .orElseGet(() -> {
                        SanPham sp = sanPhamRepository.findById(item.getBienTheId())
                                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại ID: " + item.getBienTheId()));

                        return bienTheSanPhamRepository.findAll().stream()
                                .filter(b -> b.getSanPham() != null && b.getSanPham().getSanPhamId().equals(sp.getSanPhamId()))
                                .findFirst()
                                .orElseGet(() -> {
                                    BienTheSanPham quickVariant = new BienTheSanPham();
                                    quickVariant.setSanPham(sp);
                                    quickVariant.setTenBienThe("Mặc định");
                                    quickVariant.setMaSku(sp.getMaSku() != null ? sp.getMaSku() : "SKU-" + sp.getSanPhamId());
                                    quickVariant.setGiaBan(item.getUnitPrice());
                                    return bienTheSanPhamRepository.save(quickVariant);
                                });
                    });

            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setHoaDon(hoaDon); // Tạm gán
            ct.setBienThe(bienThe);
            ct.setSoLuong(item.getQuantity());
            ct.setDonGia(item.getUnitPrice());

            BigDecimal thanhTien = item.getUnitPrice()
                    .multiply(new BigDecimal(item.getQuantity()))
                    .subtract(item.getDiscount() != null ? item.getDiscount() : BigDecimal.ZERO);
            ct.setThanhTien(thanhTien);
            return ct;
        }).collect(Collectors.toList());

        // 4. Tính toán tổng tiền sau khi đã map xong
        BigDecimal tongTamTinh = chiTietList.stream()
                .map(ChiTietHoaDon::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        hoaDon.setTamTinh(tongTamTinh);
        hoaDon.setChietKhau(request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO);
        hoaDon.setPhiShip(request.getShippingFee() != null ? request.getShippingFee() : BigDecimal.ZERO);
        hoaDon.setTongPhaiThanhToan(hoaDon.getTamTinh().subtract(hoaDon.getChietKhau()).add(hoaDon.getPhiShip()));

        HoaDon saved = hoaDonRepository.save(hoaDon);
        if (saved.getMaHoaDon() == null || saved.getMaHoaDon().isBlank()) {
            saved.setMaHoaDon(request.getMaHoaDon() != null && !request.getMaHoaDon().isBlank() ? request.getMaHoaDon() : ("HD" + saved.getHoaDonId()));
            saved = hoaDonRepository.save(saved);
        }

        // Gán lại ID hóa đơn đã lưu cho các chi tiết
        for(ChiTietHoaDon ct : chiTietList){
            ct.setHoaDon(saved);
        }
        chiTietHoaDonRepository.saveAll(chiTietList);

        googleSheetsService.appendRow("orders", List.of(
                saved.getMaHoaDon(),
                saved.getKenhBan(),
                saved.getTrangThai(),
                saved.getTongPhaiThanhToan(),
                saved.getNgayLap()
        ));
        return saved;
    }
    public void cancelOrder(Integer id, String reason, String cancelledBy) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn: " + id));
        hoaDon.setTrangThai("CANCELLED");
        hoaDon.setNgayHuy(new Timestamp(System.currentTimeMillis()));
        hoaDon.setLyDoHuy(reason);
        hoaDon.setNguoiHuy(cancelledBy);
        HoaDon saved = hoaDonRepository.save(hoaDon);

        googleSheetsService.appendRow("orders_cancel", List.of(
                saved.getMaHoaDon(),
                saved.getKenhBan(),
                saved.getNguoiHuy(),
                saved.getLyDoHuy(),
                saved.getNgayHuy()
        ));
    }
}