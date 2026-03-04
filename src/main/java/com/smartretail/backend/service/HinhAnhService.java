package com.smartretail.backend.service;

import com.smartretail.backend.entity.HinhAnhSanPham;
import com.smartretail.backend.entity.SanPham;
import com.smartretail.backend.repository.HinhAnhSanPhamRepository;
import com.smartretail.backend.repository.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HinhAnhService {

    private final HinhAnhSanPhamRepository hinhAnhRepository;
    private final SanPhamRepository sanPhamRepository;
    private final CloudinaryService cloudinaryService;

    public List<HinhAnhSanPham> getHinhAnhBySanPham(Integer sanPhamId) {
        return hinhAnhRepository.findBySanPham_SanPhamId(sanPhamId);
    }

    public Optional<HinhAnhSanPham> getHinhAnhById(Integer id) {
        return hinhAnhRepository.findById(id);
    }

    public HinhAnhSanPham uploadHinhAnh(Integer sanPhamId, MultipartFile file, Boolean laChinh) {
        // Verify product exists
        SanPham sanPham = sanPhamRepository.findById(sanPhamId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Upload to Cloudinary
        Map<String, Object> uploadResult = cloudinaryService.uploadImage(file, "products");

        // If this is the main image, update other images for this product
        if (Boolean.TRUE.equals(laChinh)) {
            List<HinhAnhSanPham> existingImages = hinhAnhRepository.findBySanPham_SanPhamId(sanPhamId);
            existingImages.forEach(img -> img.setLaChinh(false));
            hinhAnhRepository.saveAll(existingImages);
        }

        // Create image record
        HinhAnhSanPham hinhAnh = new HinhAnhSanPham();
        hinhAnh.setSanPham(sanPham);
        hinhAnh.setDuongDan((String) uploadResult.get("secure_url"));
        hinhAnh.setLaChinh(laChinh != null ? laChinh : false);

        return hinhAnhRepository.save(hinhAnh);
    }

    public HinhAnhSanPham updateHinhAnh(Integer id, MultipartFile file, Boolean laChinh) {
        HinhAnhSanPham hinhAnh = hinhAnhRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // If replacing image, delete old one from Cloudinary
        if (file != null) {
            String oldPublicId = cloudinaryService.extractPublicId(hinhAnh.getDuongDan());
            if (oldPublicId != null) {
                cloudinaryService.deleteImage(oldPublicId);
            }

            // Upload new image
            String publicId = "products/" + hinhAnh.getSanPham().getMaSku() + "_" + id;
            Map<String, Object> uploadResult = cloudinaryService.uploadImageWithPublicId(file, "products", publicId);
            hinhAnh.setDuongDan((String) uploadResult.get("secure_url"));
        }

        // If setting as main image, update other images
        if (Boolean.TRUE.equals(laChinh) && !hinhAnh.getLaChinh()) {
            List<HinhAnhSanPham> existingImages = hinhAnhRepository.findBySanPham_SanPhamId(hinhAnh.getSanPham().getSanPhamId());
            existingImages.forEach(img -> {
                if (!img.getHinhAnhId().equals(id)) {
                    img.setLaChinh(false);
                }
            });
            hinhAnhRepository.saveAll(existingImages);
            hinhAnh.setLaChinh(true);
        } else if (Boolean.FALSE.equals(laChinh)) {
            hinhAnh.setLaChinh(false);
        }

        return hinhAnhRepository.save(hinhAnh);
    }

    public void deleteHinhAnh(Integer id) {
        HinhAnhSanPham hinhAnh = hinhAnhRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Delete from Cloudinary
        String publicId = cloudinaryService.extractPublicId(hinhAnh.getDuongDan());
        if (publicId != null) {
            cloudinaryService.deleteImage(publicId);
        }

        // Delete from database
        hinhAnhRepository.delete(hinhAnh);
    }

    public HinhAnhSanPham setMainImage(Integer id) {
        HinhAnhSanPham hinhAnh = hinhAnhRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // Update other images for this product
        List<HinhAnhSanPham> existingImages = hinhAnhRepository.findBySanPham_SanPhamId(hinhAnh.getSanPham().getSanPhamId());
        existingImages.forEach(img -> img.setLaChinh(false));
        hinhAnhRepository.saveAll(existingImages);

        // Set this as main image
        hinhAnh.setLaChinh(true);
        return hinhAnhRepository.save(hinhAnh);
    }

    public Optional<HinhAnhSanPham> getMainImage(Integer sanPhamId) {
        List<HinhAnhSanPham> images = hinhAnhRepository.findBySanPham_SanPhamId(sanPhamId);
        return images.stream()
                .filter(HinhAnhSanPham::getLaChinh)
                .findFirst();
    }

    public String getOptimizedImageUrl(String originalUrl, Integer width, Integer height, Integer quality) {
        String publicId = cloudinaryService.extractPublicId(originalUrl);
        return cloudinaryService.generateOptimizedUrl(publicId, width, height, quality);
    }
}