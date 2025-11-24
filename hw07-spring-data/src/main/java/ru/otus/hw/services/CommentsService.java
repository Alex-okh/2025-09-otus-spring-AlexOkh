package ru.otus.hw.services;

import ru.otus.hw.models.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentsService {

    List<Comment> findAllByBookId(long bookId);

    Optional<Comment> findById(long id);
}
