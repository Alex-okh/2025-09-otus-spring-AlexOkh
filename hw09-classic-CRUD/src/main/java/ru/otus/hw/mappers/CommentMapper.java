package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {

    public CommentDTO commentToDTO(Comment comment) {
        return new CommentDTO(comment.getId(), comment.getText(), comment.getBook()
                                                                         .getId());
    }

    public List<CommentDTO> commentToDTO(List<Comment> comments) {
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOs.add(commentToDTO(comment));
        }
        return commentDTOs;
    }
}
