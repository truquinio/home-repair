package com.egg.homerepair.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.egg.homerepair.entity.Image;
import com.egg.homerepair.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    private ImageService imageService;

    private ImageController controller;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        controller = new ImageController(imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void databaseImageUsesStoredMimeType() throws Exception {
        Image image = new Image();
        image.setMime("image/png");
        image.setContent(new byte[] {1, 2, 3});
        when(imageService.getById("image-1")).thenReturn(image);

        mockMvc.perform(get("/image/DB/image-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(new byte[] {1, 2, 3}));
    }

    @Test
    void localImageRejectsPathTraversal() {
        assertThrows(
                ResponseStatusException.class,
                () -> controller.getLocalImage("../../application.properties"));
    }
}
