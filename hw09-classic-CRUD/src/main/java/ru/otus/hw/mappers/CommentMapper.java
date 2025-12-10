package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {

    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getBook()
                                                                         .getId());
    }

    public List<CommentDto> commentToDto(List<Comment> comments) {
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(commentToDto(comment));
        }
        return commentDtos;
    }
}
