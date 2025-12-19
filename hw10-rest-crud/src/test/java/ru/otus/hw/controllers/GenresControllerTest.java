package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GenresController.class)
@DisplayName("Контроллер жанров должен")
class GenresControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("Возвращать страницу жанров при GET /genres")
    @Test
    void genresViewTest() throws Exception {
        this.mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres"));
    }
}
