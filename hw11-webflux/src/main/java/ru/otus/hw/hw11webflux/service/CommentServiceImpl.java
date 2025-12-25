package ru.otus.hw.hw11webflux.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.dto.CommentDto;
import ru.otus.hw.hw11webflux.mappers.CommentMapper;
import ru.otus.hw.hw11webflux.repositories.CommentRepository;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Override
    public Flux<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findByBookId(bookId)
                                .map(commentMapper::toDto);
    }

    @Override
    public Mono<CommentDto> saveComment(CommentDto commentDto) {
        return null;
    }

    @Override
    public void deleteComment(CommentDto commentDto) {

    }
}
