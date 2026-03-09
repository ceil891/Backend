package com.smartretail.backend.service;

import com.smartretail.backend.dto.SanPhamDTO;
import com.smartretail.backend.entity.SanPham;
import com.smartretail.backend.entity.BienTheSanPham; // ✅ Thêm import này
import com.smartretail.backend.repository.DanhMucRepository;
import com.smartretail.backend.repository.SanPhamRepository;
import com.smartretail.backend.repository.BienTheSanPhamRepository; // ✅ Thêm Repo biến thể
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;
    private final DanhMucRepository danhMucRepository;
    private final BienTheSanPhamRepository bienTheRepository; // ✅ Inject Repo để lưu biến thể
    private final CloudinaryService cloudinaryService;
    private final GoogleSheetsService googleSheetsService;

    public List<SanPham> getAllSanPham() {
        return sanPhamRepository.findAll();
    }

    /**
     * Tạo sản phẩm mới và tự động tạo biến thể mặc định
     */
    @Transactional
    public SanPham createSanPham(SanPhamDTO dto) {
        // 1. Khởi tạo thực thể Sản phẩm từ DTO
        SanPham sanPham = new SanPham();
        sanPham.setMaSku(dto.getMaSku());
        sanPham.setTenSanPham(dto.getTenSanPham());
        sanPham.setGiaBan(dto.getGiaBan());
        sanPham.setGiaNhap(dto.getGiaNhap());
        sanPham.setHoatDong(dto.getHoatDong());
        sanPham.setThuongHieu(dto.getThuongHieu());
        sanPham.setHinhAnhUrls(dto.getHinhAnhUrls());
        
        // Mặc định đơn vị là 1 (Cái) nếu không gửi lên
        sanPham.setDonViId(dto.getDonViId() != null ? dto.getDonViId() : 1);

        // Kiểm tra danh mục
        if (dto.getDanhMucId() != null) {
            sanPham.setDanhMuc(danhMucRepository.findById(dto.getDanhMucId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục")));
        } else {
            throw new RuntimeException("Danh mục không được để trống!");
        }

        // 2. Lưu Sản phẩm gốc vào DB
        SanPham savedSp = sanPhamRepository.save(sanPham);

        // ✅ 3. TỰ ĐỘNG TẠO BIẾN THỂ MẶC ĐỊNH
        // Bước này cực kỳ quan trọng để trang Đơn hàng có thể hiển thị Tên & Số lượng
        BienTheSanPham defaultVariant = new BienTheSanPham();
        defaultVariant.setSanPham(savedSp); // Liên kết với sản phẩm vừa lưu
        defaultVariant.setMaSku(savedSp.getMaSku());
        defaultVariant.setTenBienThe(savedSp.getTenSanPham()); // Tên biến thể mặc định = tên SP
        defaultVariant.setGiaBan(savedSp.getGiaBan());
        defaultVariant.setGiaNhap(savedSp.getGiaNhap());
        defaultVariant.setMaVach(savedSp.getMaSku()); 
        defaultVariant.setHoatDong(true);
        
        // Lưu biến thể vào database
        BienTheSanPham savedVariant = bienTheRepository.save(defaultVariant);

        // ✅ Log Google Sheets (tạo sản phẩm + biến thể mặc định)
        googleSheetsService.appendRow("products", List.of(
                "CREATE_PRODUCT",
                savedSp.getSanPhamId(),
                savedSp.getMaSku(),
                savedSp.getTenSanPham(),
                savedSp.getGiaBan(),
                savedVariant.getBienTheId(),
                savedVariant.getMaSku(),
                savedVariant.getTenBienThe()
        ));

        return savedSp;
    }

    @Transactional
    public SanPham updateSanPham(Integer id, SanPhamDTO dto) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        
        sanPham.setMaSku(dto.getMaSku());
        sanPham.setTenSanPham(dto.getTenSanPham());
        sanPham.setGiaBan(dto.getGiaBan());
        sanPham.setGiaNhap(dto.getGiaNhap());
        sanPham.setHoatDong(dto.getHoatDong());
        sanPham.setThuongHieu(dto.getThuongHieu());
        sanPham.setHinhAnhUrls(dto.getHinhAnhUrls());
        
        if (dto.getDonViId() != null) {
            sanPham.setDonViId(dto.getDonViId());
        }

        if (dto.getDanhMucId() != null) {
            sanPham.setDanhMuc(danhMucRepository.findById(dto.getDanhMucId())
                .orElseThrow(() -> new RuntimeException("Danh mục không hợp lệ")));
        }
        
        // Lưu ý: Nếu bạn muốn đồng bộ giá từ SP xuống Biến thể khi sửa, 
        // bạn có thể thêm logic tìm Biến thể theo sanPhamId và cập nhật tại đây.
        
        return sanPhamRepository.save(sanPham);
    }

    public Optional<SanPham> getSanPhamById(Integer id) {
        return sanPhamRepository.findById(id);
    }

    public List<SanPham> getActiveSanPham() {
        return sanPhamRepository.findByHoatDongTrue();
    }

    public List<SanPham> searchSanPham(String keyword) {
        return sanPhamRepository.searchByKeyword(keyword);
    }

    @Transactional
    public void deleteSanPham(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm để xóa"));

        // Xóa ảnh trên Cloudinary
        if (sanPham.getHinhAnhUrls() != null) {
            for (String url : sanPham.getHinhAnhUrls()) {
                String publicId = cloudinaryService.extractPublicId(url); 
                if (publicId != null) cloudinaryService.deleteImage(publicId);
            }
        }

        // Xóa sản phẩm (Cascade sẽ tự xóa các biến thể liên quan nếu bạn cấu hình trong Entity)
        sanPhamRepository.delete(sanPham);
    }
    @Transactional
    public void deactivateSanPham(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Đặt trạng thái HoatDong thành false (Khóa)
        sanPham.setHoatDong(false);
        sanPhamRepository.save(sanPham);

        // Optional: Bạn có thể viết thêm logic khóa luôn các Biến Thể của sản phẩm này ở đây
    }

    @Transactional
    public void activateSanPham(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // Đặt trạng thái HoatDong thành true (Mở khóa)
        sanPham.setHoatDong(true);
        sanPhamRepository.save(sanPham);

        // Optional: Mở khóa luôn các Biến Thể nếu cần
    }
}