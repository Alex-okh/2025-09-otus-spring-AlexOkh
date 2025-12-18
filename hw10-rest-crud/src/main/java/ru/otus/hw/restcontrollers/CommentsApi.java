package ru.otus.hw.restcontrollers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.NewCommentDto;
import ru.otus.hw.services.CommentsService;
import java.util.List;

@RestController
@AllArgsConstructor
public class CommentsApi {

    private final CommentsService commentsService;

    @GetMapping("/api/books/{id}/comments")
    public List<CommentDto> getComments(@PathVariable long id) {
        return commentsService.findAllByBookId(id);
    }

    @PostMapping("/api/books/{bookId}/comments")
    public ResponseEntity<CommentDto> saveComment(@PathVariable long bookId, @RequestBody CommentDto commentDto) {
        var newComment = new NewCommentDto(bookId, commentDto.text());
        var savedComment = commentsService.save(newComment);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(savedComment);
    }
}
