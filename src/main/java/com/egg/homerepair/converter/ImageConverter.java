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

        if (!ALLOWED_MIME_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Formato de imagen no soportado");
        }
        if (file.getSize() > MAX_IMAGE_BYTES) {
            throw new IllegalArgumentException("La imagen supera el límite de 5 MB");
        }

        try {
            byte[] content = file.getBytes();
            if (!matchesSignature(file.getContentType(), content)) {
                throw new IllegalArgumentException(
                        "El contenido del archivo no coincide con un formato de imagen válido");
            }

            Image image = new Image();
            image.setName(safeFilename(file.getOriginalFilename()));
            image.setMime(file.getContentType());
            image.setContent(content);
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

    private boolean matchesSignature(String mime, byte[] bytes) {
        if ("image/jpeg".equals(mime)) {
            return bytes.length >= 3
                    && unsigned(bytes[0]) == 0xFF
                    && unsigned(bytes[1]) == 0xD8
                    && unsigned(bytes[2]) == 0xFF;
        }

        if ("image/png".equals(mime)) {
            int[] signature = {0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
            if (bytes.length < signature.length) {
                return false;
            }
            for (int index = 0; index < signature.length; index++) {
                if (unsigned(bytes[index]) != signature[index]) {
                    return false;
                }
            }
            return true;
        }

        if ("image/webp".equals(mime)) {
            return bytes.length >= 12
                    && bytes[0] == 'R'
                    && bytes[1] == 'I'
                    && bytes[2] == 'F'
                    && bytes[3] == 'F'
                    && bytes[8] == 'W'
                    && bytes[9] == 'E'
                    && bytes[10] == 'B'
                    && bytes[11] == 'P';
        }

        return false;
    }

    private int unsigned(byte value) {
        return value & 0xFF;
    }

    private String safeFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return "profile-image";
        }
        String normalized = filename.replace('\\', '/');
        int separator = normalized.lastIndexOf('/');
        String basename = separator >= 0 ? normalized.substring(separator + 1) : normalized;
        return basename.length() > 255 ? basename.substring(0, 255) : basename;
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
