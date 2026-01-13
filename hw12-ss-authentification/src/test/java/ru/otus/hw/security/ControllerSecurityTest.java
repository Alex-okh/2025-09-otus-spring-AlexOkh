package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.controllers.AuthorController;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.controllers.GenresController;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@WebMvcTest(controllers = {AuthorController.class, BookController.class, GenresController.class})
@Import(SecurityConfig.class)
public class ControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("Обращение на эндпоинт должно возвращать ожидаемый статус")
    @ParameterizedTest(name = "метод {0} на {1} для пользователя {2} должен вернуть {4}")
    @MethodSource("getTestData")
    void shouldReturnExpected(String method, String url, String username, String[] roles, int expectedStatus,
                              boolean checkRedirect) throws Exception {

    }

    private MockHttpServletRequestBuilder buildRequest(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodsMap =
                Map.of(
                        "get", MockMvcRequestBuilders::get,
                        "post", MockMvcRequestBuilders::post,
                        "put", MockMvcRequestBuilders::put,
                        "delete",MockMvcRequestBuilders::delete);
        return methodsMap.get(method).apply(url);

    }

    private static Stream<Arguments> getTestData() {
        return Stream.of()
    }
}
