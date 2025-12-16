package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.NewBookDto;
import ru.otus.hw.dto.NewCommentDto;
import ru.otus.hw.dto.UpdateBookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentsService;
import ru.otus.hw.services.GenreService;
import java.util.Collections;

@Controller
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    private final CommentsService commentsService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String showAllBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/create")
    public String createBook(Model model) {
        var book = new BookDto(0L, "", "", 0L, "", Collections.emptySet());
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "editbook";
    }

    @GetMapping("/books/{id}/edit")
    public String editBook(Model model, @PathVariable int id) {
        var book = bookService.findById(id);
        var authors = authorService.findAll();
        var genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "editbook";
    }

    @GetMapping("/book/{id}")
    public String showBook(Model model, @PathVariable int id) {
        var book = bookService.findById(id);
        var comments = commentsService.findAllByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "book";
    }

    @PutMapping("/book")
    public String updateBook(@Valid @ModelAttribute("book") UpdateBookDto book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var authors = authorService.findAll();
            var genres = genreService.findAll();
            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "editbook";
        }
        bookService.update(book);
        return "redirect:/books";
    }

    @PostMapping("/book")
    public String createBook(@Valid @ModelAttribute("book") NewBookDto book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var authors = authorService.findAll();
            var genres = genreService.findAll();
            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "editbook";
        }
        bookService.create(book);
        return "redirect:/books";
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @PostMapping("/book/comment")
    public String saveComment(NewCommentDto comment) {
        commentsService.save(comment);
        return "redirect:/book/" + comment.bookId();
    }

}
