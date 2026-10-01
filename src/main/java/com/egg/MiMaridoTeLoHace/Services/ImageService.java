package com.egg.MiMaridoTeLoHace.Services;

import com.egg.MiMaridoTeLoHace.Entities.Image;
import com.egg.MiMaridoTeLoHace.Repositories.ImageRepository;
import com.egg.MiMaridoTeLoHace.converters.ImageConverter;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageConverter imageConverter;

    public ImageService(ImageRepository imageRepository, ImageConverter imageConverter) {
        this.imageRepository = imageRepository;
        this.imageConverter = imageConverter;
    }

    @Transactional
    public void Save(Image image) {
        if (image != null && image.getId() == null) {
            imageRepository.save(image);
        }
    }

    @Transactional(readOnly = true)
    public Image GetById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        return imageRepository.findById(id).orElse(null);
    }

    public Image GetByName(String name) throws IOException {
        ClassPathResource resource = new ClassPathResource("static/img/" + name);
        if (!resource.exists()) {
            throw new IOException("No se encontró la imagen " + name);
        }
        return imageConverter.resourceToImage(resource);
    }

    @Transactional
    public void Delete(String id) {
        if (id != null && imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
        }
    }
}
