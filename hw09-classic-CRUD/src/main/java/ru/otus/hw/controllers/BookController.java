package ru.otus.hw.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentsService;

@Controller
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    private final CommentsService commentsService;

    private final CommentMapper commentMapper;

    @GetMapping("/books")
    public String showAllBooks(Model model) {
        var books = bookMapper.bookToDto(bookService.findAll());
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book/{id}")
    public String showBook(Model model, @PathVariable int id) {
        var book = bookMapper.bookToDto(bookService.findById(id)
                                                   .orElseThrow(() -> new EntityNotFoundException(
                                                           "Book with id %d not found.".formatted(id))));
        var comments = commentMapper.commentToDTO(commentsService.findAllByBookId(id));
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "book";
    }
}
