package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;
import java.util.List;
import java.util.Optional;

@Repository
public class JPACommentRepository implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findByBookId(long id) {
        return em.createQuery("select c from Comment c where c.book.id = :id", Comment.class)
                 .setParameter("id", id)
                 .getResultList();
    }
}
