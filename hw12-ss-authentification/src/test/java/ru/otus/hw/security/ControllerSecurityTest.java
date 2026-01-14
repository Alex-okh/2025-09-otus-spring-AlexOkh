package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.controllers.AuthorController;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.controllers.GenresController;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentsService;
import ru.otus.hw.services.GenreService;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import static java.util.Objects.nonNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthorController.class, BookController.class, GenresController.class})
@Import(SecurityConfig.class)
public class ControllerSecurityTest {

    private static final String[] USER_ROLE = {"USER"};
    private static final String[] EDITOR_ROLE = {"EDITOR"};

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private CommentsService commentsService;

    private static Stream<Arguments> getTestData() {

        return Stream.of(
                Arguments.of("put", "/book", null, null, 302, true),
                Arguments.of("put", "/book", "user", USER_ROLE, 403, false),
                Arguments.of("put", "/book", "editor", EDITOR_ROLE, 200, false),
                Arguments.of("get", "/books", null, null, 302, true),
                Arguments.of("get", "/books", null, null, 302, true),
                Arguments.of("get", "/books", null, null, 302, true)
                );
    }

    @DisplayName("Обращение на эндпоинт должно возвращать ожидаемый статус")
    @ParameterizedTest(name = "метод {0} на {1} для пользователя {2} должен вернуть {4}")
    @MethodSource("getTestData")
    void shouldReturnExpected(String method, String url, String username, String[] roles, int expectedStatus,
                              boolean checkRedirect) throws Exception {
        var request = buildRequest(method, url);
        if (nonNull(username)) {
            request = request.with(user(username).roles(roles));
        }
        ResultActions result = mvc.perform(request)
                                  .andExpect(status().is(expectedStatus));
        if (checkRedirect) {
            result.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    private MockHttpServletRequestBuilder buildRequest(String method, String url) {
        Map<String, Function<String, MockHttpServletRequestBuilder>> methodsMap = Map.of("get",
                                                                                         MockMvcRequestBuilders::get,
                                                                                         "post",
                                                                                         MockMvcRequestBuilders::post,
                                                                                         "put",
                                                                                         MockMvcRequestBuilders::put,
                                                                                         "delete",
                                                                                         MockMvcRequestBuilders::delete);
        return methodsMap.get(method)
                         .apply(url);

    }

}
