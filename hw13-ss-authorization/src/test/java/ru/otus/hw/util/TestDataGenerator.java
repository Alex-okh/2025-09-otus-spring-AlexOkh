package ru.otus.hw.util;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import java.util.List;
import java.util.stream.IntStream;

public class TestDataGenerator {
    public static List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Author(id, "Author_" + id))
                        .toList();
    }

    public static List<Genre> getDbGenres() {
        return IntStream.range(1, 7)
                        .boxed()
                        .map(id -> new Genre(id, "Genre_" + id))
                        .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Book(id, "BookTitle_" + id, dbAuthors.get(id - 1),
                                            dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2),"editor1"))
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
                        .map(id -> new Comment(id, "Comment_" + ((id % 3) + 1) + "_" + (id + 1), books.get(id % 3)))
                        .toList();
    }
}
