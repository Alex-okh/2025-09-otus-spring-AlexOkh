package ru.otus.hw.hw11webflux.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.dto.CommentDto;
import ru.otus.hw.hw11webflux.exceptions.EntityNotFoundException;
import ru.otus.hw.hw11webflux.mappers.CommentMapper;
import ru.otus.hw.hw11webflux.models.Comment;
import ru.otus.hw.hw11webflux.repositories.BookRepository;
import ru.otus.hw.hw11webflux.repositories.CommentRepository;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final BookRepository bookRepository;

    @Override
    public Flux<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findByBookId(bookId)
                                .map(commentMapper::toDto);
    }

    @Override
    public Mono<CommentDto> saveComment(CommentDto commentDto) {
        var comment = new Comment(commentDto.id(), commentDto.text(), commentDto.bookId());
        return bookRepository.findById(commentDto.bookId())
                             .switchIfEmpty(Mono.error(new EntityNotFoundException(
                                     "Book with Id %s not found".formatted(commentDto.bookId()))))
                             .flatMap(book -> commentRepository.save(comment))
                             .map(commentMapper::toDto);
    }

}
