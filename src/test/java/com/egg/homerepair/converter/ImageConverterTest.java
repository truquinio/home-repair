package com.egg.homerepair.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.egg.homerepair.entity.Image;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class ImageConverterTest {

    private final ImageConverter converter = new ImageConverter();

    @Test
    void rejectsNonImageUploads() {
        MockMultipartFile file = new MockMultipartFile(
                "img",
                "notes.txt",
                "text/plain",
                "not an image".getBytes());

        assertThrows(IllegalArgumentException.class, () -> converter.convert(file));
    }

    @Test
    void rejectsImagesLargerThanFiveMegabytes() {
        byte[] content = new byte[(5 * 1024 * 1024) + 1];
        MockMultipartFile file = new MockMultipartFile(
                "img",
                "large.png",
                "image/png",
                content);

        assertThrows(IllegalArgumentException.class, () -> converter.convert(file));
    }

    @Test
    void acceptsJpegUpload() {
        byte[] content = new byte[] {1, 2, 3};
        MockMultipartFile file = new MockMultipartFile(
                "img",
                "profile.jpg",
                "image/jpeg",
                content);

        Image image = converter.convert(file);

        assertEquals("profile.jpg", image.getName());
        assertEquals("image/jpeg", image.getMime());
        assertEquals(3, image.getContent().length);
    }
}
