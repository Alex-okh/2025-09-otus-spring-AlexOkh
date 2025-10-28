package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.Availability;
import org.springframework.shell.AvailabilityProvider;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.RegisterContext;

@Configuration
public class ShellConfig {

    @Bean
    public AvailabilityProvider shellAvailabilityProvider(RegisterContext registerContextContext,
                                                          LocalizedMessagesService messageService) {
        return () -> registerContextContext.isStudentRegistered() ? Availability.available() : Availability.unavailable(
                messageService.getMessage("Shelcommands.unregistered"));
    }
}
