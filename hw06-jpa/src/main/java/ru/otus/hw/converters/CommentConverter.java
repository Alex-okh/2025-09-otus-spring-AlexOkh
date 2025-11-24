package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {
    public String commmentToString(Comment comment) {
        return "Id: %d, Name: %s".formatted(comment.getId(), comment.getText());
    }
}
