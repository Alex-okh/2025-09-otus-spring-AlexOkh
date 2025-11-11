package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;
import java.util.List;
import java.util.Set;

@Repository
public class JPAGenreRepository implements GenreRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> findAll() {
        return List.of();
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return List.of();
    }
}
