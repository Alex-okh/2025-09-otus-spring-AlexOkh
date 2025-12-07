package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.NewCommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public List<Comment> findAllByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public void save(NewCommentDTO commentDTO) {
        var book = bookRepository.findById(commentDTO.bookId());
        var comment = new Comment(0, commentDTO.text(), book.orElseThrow(
                () -> new EntityNotFoundException("Book with id %d not found".formatted(commentDTO.bookId()))));
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }
}
