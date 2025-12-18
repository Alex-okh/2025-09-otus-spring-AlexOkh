package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.NewCommentDto;
import java.util.List;

public interface CommentsService {

    List<CommentDto> findAllByBookId(long bookId);

    CommentDto findById(long id);

    CommentDto save(NewCommentDto commentDto);
}
