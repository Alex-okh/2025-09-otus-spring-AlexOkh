package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер книг должен:")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @ParameterizedTest
    @CsvSource(delimiter = '|',
               value = {"/books|books", "/books/{id}|book", "/books/create|editbook", "/books/{id}/edit|editbook"})
    @DisplayName("Возвращать правильный view /books")
    void viewTest(String uri, String view) throws Exception {
        Long checkedId = 1L;
        this.mvc.perform(get(uri, checkedId))
                .andExpect(status().isOk())
                .andExpect(view().name(view));
    }
}