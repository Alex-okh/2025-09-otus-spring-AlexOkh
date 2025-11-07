package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations namedJdbc;

    public JdbcGenreRepository(NamedParameterJdbcOperations namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    @Override
    public List<Genre> findAll() {
        return namedJdbc.query("select id, name from genres", new GnreRowMapper());
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return new ArrayList<>();
    }

    private static class GnreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
