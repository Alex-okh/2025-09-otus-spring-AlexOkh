package ru.otus.hw.hw11webflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.dto.AuthorDto;
import ru.otus.hw.hw11webflux.dto.CommentDto;

public interface CommentService {

    Flux<CommentDto> findAllByBookId(String bookId);

    Mono<CommentDto> saveComment(CommentDto commentDto);

    void deleteComment(CommentDto commentDto);

}
