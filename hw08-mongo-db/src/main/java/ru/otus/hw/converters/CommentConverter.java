package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {
    public String commmentToString(Comment comment) {
        return "Text: %s".formatted(comment.getText());
    }
}
