package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public List<Comment> findAllByBookId(long bookId) {
        var comments = commentRepository.findByBookId(bookId);
        comments.forEach(comment ->
            comment.getBook()
                   .getGenres()
                   .size());
        return comments;
    }

    @Override
    @Transactional
    public Optional<Comment> findById(long id) {
        var comment = commentRepository.findById(id);
        comment.ifPresent(c -> c.getBook()
                                .getGenres()
                                .size());
        return comment;
    }
}
