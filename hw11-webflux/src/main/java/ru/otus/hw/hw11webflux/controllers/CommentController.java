package ru.otus.hw.hw11webflux.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.hw11webflux.dto.CommentDto;
import ru.otus.hw.hw11webflux.dto.NewCommentDto;
import ru.otus.hw.hw11webflux.models.Comment;
import ru.otus.hw.hw11webflux.service.CommentService;

@RestController
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("api/flux/books/{bookId}/comments")
    public Flux<CommentDto> getComments(@PathVariable String bookId) {
        return commentService.findAllByBookId(bookId);
    }

    @PostMapping("api/flux/books/{bookId}/comments")
    public Mono<CommentDto> saveComment(@PathVariable String bookId, @RequestBody NewCommentDto commentDto) {
        var comment = new CommentDto(null, commentDto.text(), bookId);
        return commentService.saveComment(comment);
    }
}
