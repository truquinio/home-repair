package com.egg.homerepair.Controllers;

import com.egg.homerepair.Entities.Image;
import com.egg.homerepair.Services.ImageService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/DB/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] getImageById(@PathVariable String id) {
        Image image = imageService.GetById(id);
        if (image == null || image.getContent() == null) {
            throw new ResponseStatusException(NOT_FOUND, "Imagen no encontrada");
        }
        return image.getContent();
    }

    @GetMapping("/{name:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getLocalImage(@PathVariable String name) {
        Resource resource = new ClassPathResource("static/img/" + name);
        if (!resource.exists() || !resource.isReadable()) {
            throw new ResponseStatusException(NOT_FOUND, "Imagen no encontrada");
        }

        MediaType mediaType = MediaTypeFactory.getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}

