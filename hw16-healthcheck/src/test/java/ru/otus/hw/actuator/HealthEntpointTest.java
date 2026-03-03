package ru.otus.hw.actuator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

class HealthEntpointTest {

    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest
    @DisplayName("Должны быть доступны ендпоинты мониторинга")
    @ValueSource(strings = {"/actuator", "/actuator/logfile", "/actuator/metrics", "/actuator/health"})
    void healthCheck(String endpoint) throws Exception {
        this.mockMvc.perform(get(endpoint))
                    .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Ответ health ендпоинта должен содержать чек библиотеки")
    void customHealthCheck() throws Exception {
        this.mockMvc.perform(get("/actuator/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.components.library").exists())
                    .andExpect(jsonPath("$.components.library.status").value("UP"))
                    .andExpect(jsonPath("$.components.library.details['Authors count ']").exists())
                    .andExpect(jsonPath("$.components.library.details['Books count ']").exists())
                    .andExpect(jsonPath("$.components.library.details['Genres count ']").exists());
    }
}
