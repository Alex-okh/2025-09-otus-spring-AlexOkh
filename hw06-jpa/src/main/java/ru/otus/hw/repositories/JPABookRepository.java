package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import java.util.List;
import java.util.Optional;

@Repository
public class JPABookRepository implements BookRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    @EntityGraph(value = "book-author-genres", type = EntityGraph.EntityGraphType.FETCH)
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
        public List<Book> findAll() {
        return em.createQuery("select b from Book b LEFT JOIN FETCH b.author LEFT JOIN FETCH b.genres LEFT JOIN FETCH b.comments", Book.class).getResultList();}

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public void deleteById(long id) {

    }
}
