package ru.otus.hw.hw11webflux.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.hw11webflux.models.Author;
import ru.otus.hw.hw11webflux.models.Genre;
import ru.otus.hw.hw11webflux.repositories.AuthorRepository;
import ru.otus.hw.hw11webflux.repositories.BookRepository;
import ru.otus.hw.hw11webflux.repositories.CommentRepository;
import ru.otus.hw.hw11webflux.repositories.GenreRepository;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class InitData implements ApplicationRunner {

    private final AuthorRepository authorRepository;

    //private final BookRepository bookRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final List<String> INIT_AUTHORS =  List.of(
            "Александр Пушкин",
            "Лев Толстой",
            "Фёдор Достоевский",
            "Антон Чехов",
            "Николай Гоголь",
            "Михаил Булгаков",
            "Стивен Кинг",
            "Джоан Роулинг",
            "Джордж Оруэлл",
            "Рэй Брэдбери"
                                                      );

    private final List<String> INIT_GENRES =  List.of(
            "Роман",
            "Фэнтези",
            "Детектив",
            "Научная фантастика",
            "Ужасы",
            "Классика",
            "Биография",
            "Поэзия",
            "Приключения",
            "Драма"
                                                                                   );

    @Override
    public void run(ApplicationArguments args) throws Exception {
        clearDb();
        List<Author> dbAuthors = new ArrayList<>();
        for (String author : INIT_AUTHORS) {
            dbAuthors.add(authorRepository.save(new Author(null, author)).block());
        }
        List<Genre> dbGenres = new ArrayList<>();
        for (String genre : INIT_GENRES) {
            dbGenres.add(genreRepository.save(new Genre(null, genre)).block());
        }



    }

    private void clearDb() {
        authorRepository.deleteAll();
        genreRepository.deleteAll();
        commentRepository.deleteAll();
        //bookRepository.deleteAll();
    }
}
