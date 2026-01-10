package ru.otus.hw.hw11webflux.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.hw11webflux.dto.CommentDto;
import ru.otus.hw.hw11webflux.models.Comment;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getBookId());
    }

    public List<CommentDto> toDto(List<Comment> comments) {
        var commentsDtos = new ArrayList<CommentDto>();
        for (var comment : comments) {
            commentsDtos.add(toDto(comment));
        }
        return commentsDtos;
    }
}
