package ru.otus.hw.hw11webflux.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw11webflux.dto.CommentDto;
import ru.otus.hw.hw11webflux.models.Comment;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getBookId());
    }
}
