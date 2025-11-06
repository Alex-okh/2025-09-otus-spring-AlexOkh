package ru.otus.hw.repositories;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedJdbc;

    public JdbcBookRepository(NamedParameterJdbcOperations namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            return Optional.of(namedJdbc.queryForObject("""
                                                                select books.id, title, authors.id, authors.full_name, genres.id, genres.name 
                                                                from books join authors on authors.id = author_id 
                                                                           join genres on genres.id = genre_id 
                                                                where books.id = :id""", params, new BookRowMapper()));

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return namedJdbc.query("""
                                       select books.id, title, authors.id, authors.full_name, genres.id, genres.name 
                                       from books join authors on authors.id = author_id 
                                                  join genres on genres.id = genre_id 
                                       """, new BookRowMapper());

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

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource(Map.of("title", book.getTitle(), "author_id", book.getAuthor()
                                                                                                 .getId(), "genre_id",
                                                      book.getGenre()
                                                          .getId()));
        try {
            namedJdbc.update("""
                                                       insert into books (title, author_id, genre_id)
                                                       values(:title, :author_id,:genre_id)
                                                       """, params, keyHolder);
            book.setId(keyHolder.getKeyAs(Long.class));
            return book;
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " has bad genre or author ID");
        } catch (DataAccessException e) {
            throw new RuntimeException("Database operation error while updating book " + e.getMessage());
        }
    }

    private Book update(Book book) {
        Map<String, Object> params = Map.of("id", book.getId(), "title", book.getTitle(), "author_id", book.getAuthor()
                                                                                                           .getId(),
                                            "genre_id", book.getGenre()
                                                            .getId());
        try {
            int updatedRows = namedJdbc.update("""
                                                       update books
                                                       set title = :title, author_id = :author_id, genre_id = :genre_id
                                                       where id = :id
                                                       """, params);
            if (updatedRows == 0) {
                throw new EntityNotFoundException("Book with id " + book.getId() + " not found");
            }
            return book;
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " has bad genre or author ID");
        } catch (DataAccessException e) {
            throw new RuntimeException("Database operation error while updating book " + e.getMessage());
        }
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("books.id");
            long genreId = rs.getLong("genres.id");
            long authorId = rs.getLong("authors.id");
            String title = rs.getString("title");
            String author = rs.getString("authors.full_name");
            String genre = rs.getString("genres.name");
            return new Book(id, title, new Author(authorId, author), new Genre(genreId, genre));
        }
    }
}
