package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import java.util.List;
import java.util.Optional;
import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public class JPABookRepository implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-authors-genres");
        TypedQuery<Book> query = em.createQuery("select b from Book b where b.id = :id", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        var book = query.setParameter("id", id)
                        .getSingleResult();
        if (book != null) {
            Hibernate.initialize(book.getComments());
        }
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
//        EntityGraph<?> entityGraph = em.getEntityGraph("book-authors-genres");
        TypedQuery<Book> query = em.createQuery("""
                                                       select distinct b from Book b
                                                       left join fetch b.author
                                                      left join fetch b.genres
                                                      left join fetch b.comments
                                                        """, Book.class);
//        query.setHint(FETCH.getKey(), entityGraph);
        List<Book> books = query.getResultList();

//        for (Book book : books) {
//            Hibernate.initialize(book.getComments());
//        }
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(Book.class, id));
    }
}
