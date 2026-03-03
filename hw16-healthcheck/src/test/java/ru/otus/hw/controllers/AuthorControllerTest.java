package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthorController.class)
@DisplayName("Контроллер авторов должен")
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("Возвращать страницу авторов при GET /authors")
    @Test
    void authorViewTest() throws Exception {
        this.mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"));
    }
}