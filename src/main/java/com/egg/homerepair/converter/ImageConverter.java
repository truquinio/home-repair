package com.egg.homerepair.converter;

import com.egg.homerepair.entity.Image;
import java.io.IOException;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageConverter implements Converter<MultipartFile, Image> {

    static final long MAX_IMAGE_BYTES = 5L * 1024 * 1024;
    private static final Set<String> ALLOWED_MIME_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp");

    @Override
    public Image convert(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        validateUpload(file);

        try {
            Image image = new Image();
            image.setName(safeFilename(file.getOriginalFilename()));
            image.setMime(file.getContentType());
            image.setContent(file.getBytes());
            return image;
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo procesar la imagen", e);
        }
    }

    public Image resourceToImage(Resource resource) throws IOException {
        if (resource == null || !resource.exists()) {
            throw new IOException("Recurso de imagen no encontrado");
        }

        String mime = getMimeType(resource.getFilename());
        if (mime == null) {
            throw new IOException("Formato de imagen no soportado");
        }

        Image image = new Image();
        image.setMime(mime);
        image.setName(resource.getFilename());

        try (var input = resource.getInputStream()) {
            image.setContent(IOUtils.toByteArray(input));
        }

        return image;
    }

    private void validateUpload(MultipartFile file) {
        if (!ALLOWED_MIME_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Formato de imagen no soportado");
        }
        if (file.getSize() > MAX_IMAGE_BYTES) {
            throw new IllegalArgumentException("La imagen supera el límite de 5 MB");
        }
    }

    private String safeFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "profile-image";
        }
        String normalized = filename.replace('\\', '/');
        int separator = normalized.lastIndexOf('/');
        return separator >= 0 ? normalized.substring(separator + 1) : normalized;
    }

    private String getMimeType(String filename) {
        if (filename == null) {
            return null;
        }

        int extensionIndex = filename.lastIndexOf('.');
        if (extensionIndex < 0 || extensionIndex == filename.length() - 1) {
            return null;
        }

        String extension = filename.substring(extensionIndex + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "webp":
                return "image/webp";
            default:
                return null;
        }
    }
}
