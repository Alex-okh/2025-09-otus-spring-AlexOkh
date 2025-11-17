package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }
}
