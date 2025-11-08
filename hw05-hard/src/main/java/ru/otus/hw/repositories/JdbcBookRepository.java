package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedJdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        var book = namedJdbc.query(""" 
                                           select books.id, title, authors.id, full_name, genres.id, genres.name
                                           from books join authors on books.author_id = authors.id
                                                      join books_genres on books.id = book_id
                                                      join genres on genre_id = genres.id
                                           where books.id = :id""",
                                   params, new BookResultSetExtractor());
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var books = getAllBooksWithoutGenres();
        var relations = getAllGenreRelations();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        namedJdbc.update("delete from books where id = :id", params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedJdbc.query("""
                                       select books.id, title, authors.id, full_name
                                           from books join authors on books.author_id = authors.id
                                       """, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return namedJdbc.query("""
                                       select book_id, genre_id
                                           from books_genres
                                       """, Collections.emptyMap(),
                               (rs, rn) -> new BookGenreRelation(rs.getLong("book_id"),
                                                                 rs.getLong("genre_id")));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres, List<BookGenreRelation> relations) {
        for (var book : booksWithoutGenres) {
            relations.stream()
                     .filter(rel -> book.getId() == rel.bookId)
                     .flatMap(rel -> genres.stream()
                                           .filter(genre -> genre.getId() == rel.genreId))
                     .forEach(book.getGenres()::add);
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource(Map.of("title", book.getTitle(),
                                                      "author_id", book.getAuthor().getId()));
        namedJdbc.update("""
                                 insert into books (title, author_id)
                                 values(:title, :author_id)
                                 """, params, keyHolder);
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        Map<String, Object> params = Map.of("book_id", book.getId(),
                                            "title", book.getTitle(),
                                            "author_id",book.getAuthor().getId());
        var upatedRows = namedJdbc.update(""" 
                                                  update books set
                                                    title = :title,
                                                    author_id = :author_id
                                                    where id = :book_id
                                                          """, params);
        if (upatedRows == 0) {
            throw new EntityNotFoundException("Update failed. No book found with id " + book.getId());
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        Map<String, Object>[] batchParams = new Map[book.getGenres().size()];
        for (int i = 0; i < batchParams.length; i++) {
            Map<String, Object> params = new HashMap<>();
            params.put("bookid", book.getId());
            params.put("genreid", book.getGenres()
                                      .get(i)
                                      .getId());
            batchParams[i] = params;
        }
        namedJdbc.batchUpdate("""
                                      insert into books_genres (book_id, genre_id)
                                      values (:bookid, :genreid)
                                      """, batchParams);
    }

    private void removeGenresRelationsFor(Book book) {
        Map<String, Object> params = Collections.singletonMap("id", book.getId());
        namedJdbc.update("delete from books_genres where book_id = :id", params);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("books.id");
            long authorId = rs.getLong("authors.id");
            String title = rs.getString("title");
            String fullName = rs.getString("full_name");
            List<Genre> genres = new ArrayList<>();
            return new Book(bookId, title, new Author(authorId, fullName), genres);
        }
    }

    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException {
            if (!rs.isBeforeFirst() && rs.getRow() == 0) {
                return null;
            }
            Book book = null;
            while (rs.next()) {
                if (book == null) {
                    book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(new Author(rs.getLong("authors.id"), rs.getString("full_name")));
                    book.setGenres(new ArrayList<>());
                }
                Genre genre = new Genre(rs.getLong("genres.id"), rs.getString("genres.name"));
                book.getGenres()
                    .add(genre);
            }
            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
