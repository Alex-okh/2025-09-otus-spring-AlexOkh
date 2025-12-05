package ru.otus.hw.services;

import ru.otus.hw.models.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentsService {

    List<Comment> findAllByBookId(String bookId);

    Comment createCommentByBookId(String text, String bookId);

    void deleteCommentById(String id);

    Comment updateCommentById(String text, String id);

    Optional<Comment> findCommentById(String id);



}
