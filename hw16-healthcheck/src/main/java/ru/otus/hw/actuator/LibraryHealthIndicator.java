package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@Component
@RequiredArgsConstructor

public class LibraryHealthIndicator implements HealthIndicator {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }

    @Override
    public Health health() {
        try {
            var authors = authorRepository.count();
            var books = bookRepository.count();
            var genres = genreRepository.count();
            var healthBuilder = (authors > 0 && genres > 0 && books > 0) ? Health.up() : Health.down();

            return healthBuilder.withDetail("Authors count ", authors)
                                .withDetail("Books count ", books)
                                .withDetail("Genres count ", genres)
                                .build();
        } catch (Exception e) {
            return Health.down(e)
                         .withDetail("message","Database access failed")
                         .build();
        }

    }
}
