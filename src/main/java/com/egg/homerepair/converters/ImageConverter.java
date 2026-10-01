package com.egg.homerepair.converters;

import com.egg.homerepair.Entities.Image;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageConverter implements Converter<MultipartFile, Image> {

    @Override
    public Image convert(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Image image = new Image();
            image.setName(file.getOriginalFilename());
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
            default:
                return null;
        }
    }
}

