package com.smartretail.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Upload image to Cloudinary
     * @param file Multipart file to upload
     * @param folder Folder name in Cloudinary (e.g., "products", "categories")
     * @return Map containing upload result
     */
public Map<String, Object> uploadImage(MultipartFile file, String folder) {
    try {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", "smart-retail/" + folder,
                "resource_type", "auto",
                "quality", "auto",
                "format", "webp"
        );

        Map<String, Object> result =
                cloudinary.uploader().upload(file.getBytes(), uploadParams);

        log.info("Image uploaded successfully: {}", result.get("secure_url"));
        return result;

    } catch (Exception e) {   // ✅ đổi chỗ này
        log.error("Cloudinary upload error", e);
        throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
    }
}
    /**
     * Upload image with custom public ID
     * @param file Multipart file to upload
     * @param folder Folder name in Cloudinary
     * @param publicId Custom public ID for the image
     * @return Map containing upload result
     */
    public Map<String, Object> uploadImageWithPublicId(MultipartFile file, String folder, String publicId) {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", "smart-retail/" + folder,
                "public_id", publicId,
                "resource_type", "auto",
                "quality", "auto",
                "format", "webp",
                "overwrite", true
            );

            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            log.info("Image uploaded successfully with custom public ID: {}", result.get("secure_url"));
            return result;

        } catch (IOException e) {
            log.error("Failed to upload image with custom public ID to Cloudinary", e);
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    /**
     * Delete image from Cloudinary
     * @param publicId Public ID of the image to delete
     * @return Map containing delete result
     */
    public Map<String, Object> deleteImage(String publicId) {
        try {
            Map<String, Object> deleteParams = ObjectUtils.asMap(
                "resource_type", "image"
            );

            Map<String, Object> result = cloudinary.uploader().destroy(publicId, deleteParams);
            log.info("Image deleted successfully from Cloudinary: {}", publicId);
            return result;

        } catch (Exception e) {
            log.error("Failed to delete image from Cloudinary: {}", publicId, e);
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    /**
     * Extract public ID from Cloudinary URL
     * @param cloudinaryUrl Full Cloudinary URL
     * @return Public ID of the image
     */
    public String extractPublicId(String cloudinaryUrl) {
        if (cloudinaryUrl == null || cloudinaryUrl.isEmpty()) {
            return null;
        }

        try {
            // Extract public ID from URL like: https://res.cloudinary.com/cloud-name/image/upload/v123456789/smart-retail/products/image.jpg
            int uploadIndex = cloudinaryUrl.indexOf("/upload/");
            if (uploadIndex == -1) {
                return null;
            }

            String afterUpload = cloudinaryUrl.substring(uploadIndex + 8); // Skip "/upload/"
            int versionStart = afterUpload.indexOf("/v");
            if (versionStart != -1) {
                // Skip version number
                int nextSlash = afterUpload.indexOf("/", versionStart + 1);
                if (nextSlash != -1) {
                    return afterUpload.substring(nextSlash + 1);
                }
            }

            // Fallback: find the last part after upload/
            return afterUpload;

        } catch (Exception e) {
            log.error("Failed to extract public ID from URL: {}", cloudinaryUrl, e);
            return null;
        }
    }

    /**
     * Generate optimized image URL with transformations
     * @param publicId Public ID of the image
     * @param width Desired width
     * @param height Desired height
     * @param quality Quality setting (1-100)
     * @return Optimized Cloudinary URL
     */
    public String generateOptimizedUrl(String publicId, Integer width, Integer height, Integer quality) {
        if (publicId == null) {
            return null;
        }

        String transformations = "";
        if (width != null || height != null || quality != null) {
            transformations = "/";
            if (width != null) {
                transformations += "w_" + width;
            }
            if (height != null) {
                transformations += (width != null ? "," : "") + "h_" + height;
            }
            if (quality != null) {
                transformations += ((width != null || height != null) ? "," : "") + "q_" + quality;
            }
            transformations += ",f_webp";
        }

        return String.format("https://res.cloudinary.com/%s/image/upload%s/smart-retail/%s",
            cloudinary.config.cloudName, transformations, publicId);
    }
}