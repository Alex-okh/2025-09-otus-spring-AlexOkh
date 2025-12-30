package ru.otus.hw.hw11webflux.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.models.Author;
import ru.otus.hw.hw11webflux.models.Book;
import ru.otus.hw.hw11webflux.models.Comment;
import ru.otus.hw.hw11webflux.models.Genre;
import ru.otus.hw.hw11webflux.repositories.AuthorRepository;
import ru.otus.hw.hw11webflux.repositories.BookRepository;
import ru.otus.hw.hw11webflux.repositories.CommentRepository;
import ru.otus.hw.hw11webflux.repositories.GenreRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @EventListener(ApplicationReadyEvent.class)
    public Mono<Void> initializeData() {
        log.info("Starting data initialization...");

        // Очищаем коллекции в правильном порядке (из-за ссылок)
        return cleanCollections().then(createAuthors())
                                 .then(createGenres())
                                 .then(createBooks())
                                 .then(createComments())
                                 .doOnSuccess(v -> log.info("Data initialization completed successfully"))
                                 .doOnError(e -> log.error("Error during data initialization: {}", e.getMessage()));
    }

    private Mono<Void> cleanCollections() {
        return commentRepository.deleteAll()
                                .then(bookRepository.deleteAll())
                                .then(authorRepository.deleteAll())
                                .then(genreRepository.deleteAll());
    }

    private Mono<Map<Integer, Author>> createAuthors() {
        List<String> authorNames = List.of("Александр Пушкин", "Лев Толстой", "Фёдор Достоевский", "Антон Чехов",
                                           "Николай Гоголь", "Михаил Булгаков", "Стивен Кинг", "Джоан Роулинг",
                                           "Джордж Оруэлл", "Рэй Брэдбери");

        Map<Integer, Author> authorsMap = new ConcurrentHashMap<>();

        return Flux.fromIterable(authorNames)
                   .index()
                   .flatMap(tuple -> {
                       String name = tuple.getT2();
                       int index = tuple.getT1()
                                        .intValue() + 1;
                       Author author = new Author(null, name);
                       return authorRepository.save(author)
                                              .doOnNext(savedAuthor -> authorsMap.put(index, savedAuthor));
                   })
                   .then(Mono.just(authorsMap))
                   .doOnNext(map -> log.info("Created {} authors", map.size()));
    }

    private Mono<Map<Integer, Genre>> createGenres() {
        List<String> genreNames = List.of("Роман", "Фэнтези", "Детектив", "Научная фантастика", "Ужасы", "Классика",
                                          "Биография", "Поэзия", "Приключения", "Драма");

        Map<Integer, Genre> genresMap = new ConcurrentHashMap<>();

        return Flux.fromIterable(genreNames)
                   .index()
                   .flatMap(tuple -> {
                       String name = tuple.getT2();
                       int index = tuple.getT1()
                                        .intValue() + 1;
                       Genre genre = new Genre(null, name);
                       return genreRepository.save(genre)
                                             .doOnNext(savedGenre -> genresMap.put(index, savedGenre));
                   })
                   .then(Mono.just(genresMap))
                   .doOnNext(map -> log.info("Created {} genres", map.size()));
    }

    private Mono<Void> createBooks() {
        return Mono.zip(createAuthors(), createGenres())
                   .flatMap(tuple -> {
                       Map<Integer, Author> authors = tuple.getT1();
                       Map<Integer, Genre> genres = tuple.getT2();

                       // Создаем все книги
                       return Flux.just(
                                          // Александр Пушкин (3 книги)
                                          createBook("Евгений Онегин", authors.get(1), Set.of(genres.get(6), genres.get(8))),
                                          createBook("Капитанская дочка", authors.get(1), Set.of(genres.get(6), genres.get(9))),
                                          createBook("Пиковая дама", authors.get(1), Set.of(genres.get(6), genres.get(5))),

                                          // Лев Толстой (3 книги)
                                          createBook("Война и мир", authors.get(2),
                                                     Set.of(genres.get(1), genres.get(6), genres.get(10))),
                                          createBook("Анна Каренина", authors.get(2), Set.of(genres.get(1), genres.get(6))),
                                          createBook("Воскресение", authors.get(2), Set.of(genres.get(1), genres.get(6))),

                                          // Фёдор Достоевский (3 книги)
                                          createBook("Преступление и наказание", authors.get(3),
                                                     Set.of(genres.get(1), genres.get(6), genres.get(10))),
                                          createBook("Идиот", authors.get(3), Set.of(genres.get(1), genres.get(6))),
                                          createBook("Братья Карамазовы", authors.get(3), Set.of(genres.get(1), genres.get(6))),

                                          // Антон Чехов (3 книги)
                                          createBook("Вишнёвый сад", authors.get(4), Set.of(genres.get(10), genres.get(6))),
                                          createBook("Три сестры", authors.get(4), Set.of(genres.get(10))),
                                          createBook("Дама с собачкой", authors.get(4), Set.of(genres.get(1), genres.get(6))),

                                          // Николай Гоголь (3 книги)
                                          createBook("Мёртвые души", authors.get(5), Set.of(genres.get(1), genres.get(6))),
                                          createBook("Ревизор", authors.get(5), Set.of(genres.get(10), genres.get(4))),
                                          createBook("Вечера на хуторе близ Диканьки", authors.get(5),
                                                     Set.of(genres.get(2), genres.get(9))),

                                          // Михаил Булгаков (3 книги)
                                          createBook("Мастер и Маргарита", authors.get(6),
                                                     Set.of(genres.get(1), genres.get(2), genres.get(4))),
                                          createBook("Собачье сердце", authors.get(6), Set.of(genres.get(4), genres.get(5))),
                                          createBook("Белая гвардия", authors.get(6), Set.of(genres.get(1), genres.get(10))),

                                          // Стивен Кинг (3 книги)
                                          createBook("Оно", authors.get(7), Set.of(genres.get(5), genres.get(2))),
                                          createBook("Сияние", authors.get(7), Set.of(genres.get(5), genres.get(1))),
                                          createBook("Зелёная миля", authors.get(7), Set.of(genres.get(1), genres.get(10))),

                                          // Джоан Роулинг (3 книги)
                                          createBook("Гарри Поттер и философский камень", authors.get(8),
                                                     Set.of(genres.get(2), genres.get(9))),
                                          createBook("Гарри Поттер и узник Азкабана", authors.get(8),
                                                     Set.of(genres.get(2), genres.get(9))),
                                          createBook("Гарри Поттер и Дары Смерти", authors.get(8),
                                                     Set.of(genres.get(2), genres.get(9))),

                                          // Джордж Оруэлл (2 книги)
                                          createBook("1984", authors.get(9), Set.of(genres.get(1), genres.get(4))),
                                          createBook("Скотный двор", authors.get(9), Set.of(genres.get(1), genres.get(10))),

                                          // Рэй Брэдбери (3 книги)
                                          createBook("451 градус по Фаренгейту", authors.get(10),
                                                     Set.of(genres.get(1), genres.get(4))),
                                          createBook("Марсианские хроники", authors.get(10), Set.of(genres.get(4), genres.get(1))),
                                          createBook("Вино из одуванчиков", authors.get(10), Set.of(genres.get(1), genres.get(6))),

                                          // Дополнительные книги
                                          createBook("Властелин колец: Братство кольца", authors.get(8),
                                                     Set.of(genres.get(2), genres.get(9))),
                                          createBook("Граф Монте-Кристо", authors.get(2),
                                                     Set.of(genres.get(1), genres.get(9), genres.get(3))))
                                  .flatMap(bookRepository::save)
                                  .collectList()
                                  .doOnNext(books -> log.info("Created {} books", books.size()))
                                  .then();
                   });
    }

    private Mono<Void> createComments() {
        // Этот метод должен быть вызван после создания книг
        return bookRepository.findAll()
                             .collectList()
                             .flatMapMany(books -> Flux.fromIterable(books)
                                                       .flatMap(book -> {
                                                           List<Comment> comments = createCommentsForBook(book);
                                                           return commentRepository.saveAll(comments);
                                                       }))
                             .collectList()
                             .doOnNext(comments -> log.info("Created {} comments", comments.size()))
                             .then();
    }

    private List<Comment> createCommentsForBook(Book book) {
        // Простая логика для создания комментариев
        // В реальном приложении вы бы создавали разные комментарии для разных книг
        return List.of(new Comment(null, "Отличная книга!", book.getId()),
                       new Comment(null, "Очень рекомендую к прочтению", book.getId()),
                       new Comment(null, "Интересный сюжет и глубокие персонажи", book.getId()));
    }

    private Book createBook(String title, Author author, Set<Genre> genres) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenres(genres);
        return book;
    }
}