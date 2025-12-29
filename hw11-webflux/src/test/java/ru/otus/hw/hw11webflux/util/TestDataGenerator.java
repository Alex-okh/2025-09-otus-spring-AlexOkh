package ru.otus.hw.hw11webflux.util;

import ru.otus.hw.hw11webflux.models.Author;
import ru.otus.hw.hw11webflux.models.Book;
import ru.otus.hw.hw11webflux.models.Comment;
import ru.otus.hw.hw11webflux.models.Genre;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class TestDataGenerator {
    public static List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Author(String.valueOf(id), "Author_" + id))
                        .toList();
    }

    public static List<Genre> getDbGenres() {
        return IntStream.range(1, 7)
                        .boxed()
                        .map(id -> new Genre(String.valueOf(id), "Genre_" + id))
                        .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Book(String.valueOf(id), "BookTitle_" + id, dbAuthors.get(id - 1),
                                            Set.copyOf(dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2))))
                        .toList();
    }

    public static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<Comment> getDbcomments() {
        var books = getDbBooks();
        return getDbComments(books);
    }

    private static List<Comment> getDbComments(List<Book> books) {
        return IntStream.range(0, 8)
                        .boxed()
                        .map(id -> new Comment(String.valueOf(id), "Comment_" + ((id % 3) + 1) + "_" + (id + 1),
                                               books.get(id % 3)
                                                    .getId()))
                        .toList();
    }
}
