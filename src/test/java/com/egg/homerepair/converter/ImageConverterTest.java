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
        content[0] = (byte) 0xFF;
        content[1] = (byte) 0xD8;
        content[2] = (byte) 0xFF;
        MockMultipartFile file = new MockMultipartFile(
                "img",
                "large.jpg",
                "image/jpeg",
                content);

        assertThrows(IllegalArgumentException.class, () -> converter.convert(file));
    }

    @Test
    void rejectsSpoofedImageMimeType() {
        MockMultipartFile file = new MockMultipartFile(
                "img",
                "payload.jpg",
                "image/jpeg",
                "not really a jpeg".getBytes());

        assertThrows(IllegalArgumentException.class, () -> converter.convert(file));
    }

    @Test
    void acceptsJpegUploadWithValidSignature() {
        byte[] content = new byte[] {
                (byte) 0xFF,
                (byte) 0xD8,
                (byte) 0xFF,
                (byte) 0xE0,
                0x00,
                0x10
        };
        MockMultipartFile file = new MockMultipartFile(
                "img",
                "profile.jpg",
                "image/jpeg",
                content);

        Image image = converter.convert(file);

        assertEquals("profile.jpg", image.getName());
        assertEquals("image/jpeg", image.getMime());
        assertEquals(content.length, image.getContent().length);
    }

    @Test
    void stripsPathInformationFromFilename() {
        byte[] content = new byte[] {
                (byte) 0x89,
                0x50,
                0x4E,
                0x47,
                0x0D,
                0x0A,
                0x1A,
                0x0A
        };
        MockMultipartFile file = new MockMultipartFile(
                "img",
                "../../avatar.png",
                "image/png",
                content);

        Image image = converter.convert(file);

        assertEquals("avatar.png", image.getName());
    }
}
