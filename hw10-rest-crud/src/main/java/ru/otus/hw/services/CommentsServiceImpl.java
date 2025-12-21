package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.NewCommentDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public List<CommentDto> findAllByBookId(long bookId) {
        return commentMapper.commentToDto(commentRepository.findByBookId(bookId));
    }

    @Override
    public CommentDto save(NewCommentDto commentDto) {
        var book = bookRepository.findById(commentDto.bookId());
        var comment = new Comment(0, commentDto.text(), book.orElseThrow(
                () -> new BookNotFoundException("Book with id %d not found".formatted(commentDto.bookId()))));
        commentRepository.save(comment);
        return commentMapper.commentToDto(comment);
    }

    @Override
    @Transactional
    public CommentDto findById(long id) {
        return commentMapper.commentToDto(commentRepository.findById(id)
                                                           .orElseThrow(() -> new EntityNotFoundException(
                                                                   "Comment with id %d not found".formatted(id))));
    }
}
