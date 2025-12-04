package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public List<Comment> findAllByBookId(String bookId) {
        var book = bookRepository.findById(bookId);
        var comments = commentRepository.findByBookId(bookId);
        if (book.isPresent()) {
            for (Comment comment : comments) {
                comment.setBook(book.get());
            }
        }
        return comments;
    }

    @Override
    @Transactional
    public Comment createCommentByBookId(String text, String bookId) {
        var book = bookRepository.findById(bookId)
                                 .orElseThrow(() -> new EntityNotFoundException(
                                         "Book with id %s not found".formatted(bookId)));
        var comment = new Comment(null, text, book);
        return commentRepository.insert(comment);
    }

    @Override
    @Transactional
    public void deleteCommentById(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Comment updateCommentById(String text, String id) {
        var comment = commentRepository.findById(id)
                                       .orElseThrow(() -> new EntityNotFoundException(
                                               "Comment with id %s not found".formatted(id)));
        comment.setText(text);

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Optional<Comment> findCommentById(String id) {
        return commentRepository.findById(id);
    }
}
