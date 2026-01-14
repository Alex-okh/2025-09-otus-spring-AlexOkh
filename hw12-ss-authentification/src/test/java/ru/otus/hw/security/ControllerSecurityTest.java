package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.controllers.AuthorController;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.controllers.GenresController;
import ru.otus.hw.controllers.LoginController;
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

@WebMvcTest(controllers = {AuthorController.class, BookController.class, GenresController.class, LoginController.class})
@Import(SecurityConfig.class)
class ControllerSecurityTest {

    private static final String USER_ROLE = "USER";
    private static final String EDITOR_ROLE = "EDITOR";

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

        return Stream.of(Arguments.of("put", "/book", null, null, 302, true),
                         Arguments.of("put", "/book", "user", USER_ROLE, 403, false),
                         Arguments.of("put", "/book", "editor", EDITOR_ROLE, 200, false),
                         Arguments.of("post", "/book", null, null, 302, true),
                         Arguments.of("post", "/book", "user", USER_ROLE, 403, false),
                         Arguments.of("post", "/book", "editor", EDITOR_ROLE, 200, false),
                         Arguments.of("post", "/book/comment", null, null, 302, true),
                         Arguments.of("post", "/book/comment", "user", USER_ROLE, 302, false),
                         Arguments.of("post", "/book/comment", "editor", EDITOR_ROLE, 302, false),
                         Arguments.of("delete", "/books/1", null, null, 302, true),
                         Arguments.of("delete", "/books/1", "user", USER_ROLE, 403, false),
                         Arguments.of("delete", "/books/1", "editor", EDITOR_ROLE, 302, false),

                         Arguments.of("get", "/books", null, null, 302, true),
                         Arguments.of("get", "/books", "user", USER_ROLE, 200, false),
                         Arguments.of("get", "/books", "editor", EDITOR_ROLE, 200, false),
                         Arguments.of("get", "/books/create", null, null, 302, true),
                         Arguments.of("get", "/books/create", "user", USER_ROLE, 403, false),
                         Arguments.of("get", "/books/create", "editor", EDITOR_ROLE, 200, false),
                         Arguments.of("get", "/books/1/edit", null, null, 302, true),
                         Arguments.of("get", "/books/1/edit", "user", USER_ROLE, 403, false),
                         Arguments.of("get", "/books/1/edit", "editor", EDITOR_ROLE, 200, false),
                         Arguments.of("get", "/book/1", null, null, 302, true),
                         Arguments.of("get", "/book/1", "user", USER_ROLE, 200, false),
                         Arguments.of("get", "/book/1", "editor", EDITOR_ROLE, 200, false),
                         Arguments.of("get", "/authors", null, null, 302, true),
                         Arguments.of("get", "/authors", "user", USER_ROLE, 200, false),
                         Arguments.of("get", "/authors", "editor", EDITOR_ROLE, 200, false),
                         Arguments.of("get", "/genres", null, null, 302, true),
                         Arguments.of("get", "/genres", "user", USER_ROLE, 200, false),
                         Arguments.of("get", "/genres", "editor", EDITOR_ROLE, 200, false),
                         Arguments.of("get", "/login", null, null, 200, false),
                         Arguments.of("get", "/login", "user", USER_ROLE, 200, false),
                         Arguments.of("get", "/login", "editor", EDITOR_ROLE, 200, false),
                         Arguments.of("get", "/", null, null, 200, false),
                         Arguments.of("get", "/", "user", USER_ROLE, 200, false),
                         Arguments.of("get", "/", "editor", EDITOR_ROLE, 200, false));
    }

    @DisplayName("Обращение на эндпоинт должно возвращать ожидаемый статус")
    @ParameterizedTest(name = "метод {0} на {1} для пользователя {2} должен вернуть {4}")
    @MethodSource("getTestData")
    void shouldReturnExpected(String method, String url, String username, String role, int expectedStatus,
                              boolean checkRedirect) throws Exception {
        var request = buildRequest(method, url);
        if (nonNull(username)) {
            request = request.with(user(username).authorities(new SimpleGrantedAuthority(role)));
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
