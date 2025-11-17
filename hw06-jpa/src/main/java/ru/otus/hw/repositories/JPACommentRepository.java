package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;
import java.util.List;
import java.util.Optional;
import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public class JPACommentRepository implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Comment> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("comment-book");
        var comment = em.createQuery("select c from Comment c where c.id = :id", Comment.class)
                        .setParameter("id", id)
                        .setHint(FETCH.getKey(), entityGraph)
                        .getResultList();
        return comment.stream()
                      .findFirst();
    }

    @Override
    public List<Comment> findByBookId(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("comment-book");
        return em.createQuery("select c from Comment c where c.book.id = :id", Comment.class)
                 .setParameter("id", id)
                 .setHint(FETCH.getKey(), entityGraph)
                 .getResultList();
    }
}
