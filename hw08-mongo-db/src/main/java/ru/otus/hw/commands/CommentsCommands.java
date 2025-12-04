package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentsService;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentsCommands {

    private final CommentsService commentsService;

    private final BookService bookService;

    private final CommentConverter converter;

    @ShellMethod(value = "Find all comments for book id", key = "cbi")
    public String findAllCommentsByBookId(String bookId) {
        var book = bookService.findById(bookId);
        if (book.isEmpty()) {
            return "Book with id %s not found".formatted(bookId);
        }
        String bookTitle = book.get()
                               .getTitle();
        String comments = commentsService.findAllByBookId(bookId)
                                         .stream()
                                         .map(converter::commmentToString)
                                         .collect(Collectors.joining("," + System.lineSeparator()));
        return "Comments to book: " + bookTitle + "\n" + comments;
    }

    @ShellMethod(value = "Add comment by book id", key = "abi")
    public String addCommentByBookId(String text, String bookId) {
        var savedComment = commentsService.createCommentByBookId(text, bookId);
        return converter.commmentToString(savedComment);
    }

    @ShellMethod(value = "Update comment by id", key = "uci")
    public String updateCommentById(String text, String id) {
        var savedComment = commentsService.updateCommentById(text, id);
        return converter.commmentToString(savedComment);
    }

    @ShellMethod(value = "Delete comment by id", key = "dci")
    public String deleteCommentById(String id) {
        commentsService.deleteCommentById(id);
        return "Comment with id %s deleted".formatted(id);
    }

}
