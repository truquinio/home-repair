package com.egg.homerepair.controller;

import com.egg.homerepair.entity.Image;
import com.egg.homerepair.service.ImageService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/image")
public class ImageController {

    private static final String SAFE_RESOURCE_NAME = "[A-Za-z0-9._-]+";

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/DB/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImageById(@PathVariable String id) {
        Image image = imageService.getById(id);
        if (image == null || image.getContent() == null) {
            throw new ResponseStatusException(NOT_FOUND, "Imagen no encontrada");
        }

        MediaType mediaType;
        try {
            mediaType = image.getMime() == null
                    ? MediaType.APPLICATION_OCTET_STREAM
                    : MediaType.parseMediaType(image.getMime());
        } catch (IllegalArgumentException ex) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.X_CONTENT_TYPE_OPTIONS, "nosniff")
                .body(image.getContent());
    }

    @GetMapping("/{name:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getLocalImage(@PathVariable String name) {
        if (name == null
                || !name.matches(SAFE_RESOURCE_NAME)
                || name.contains("..")) {
            throw new ResponseStatusException(BAD_REQUEST, "Nombre de recurso inválido");
        }

        Resource resource = new ClassPathResource("static/img/" + name);
        if (!resource.exists() || !resource.isReadable()) {
            throw new ResponseStatusException(NOT_FOUND, "Imagen no encontrada");
        }

        MediaType mediaType = MediaTypeFactory.getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.X_CONTENT_TYPE_OPTIONS, "nosniff")
                .body(resource);
    }
}
