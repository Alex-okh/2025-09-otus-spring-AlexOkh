package ru.otus.hw.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentsService;
import ru.otus.hw.services.GenreService;

@Controller
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    private final CommentsService commentsService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String showAllBooks() {
        return "books";
    }

    @GetMapping("/books/create")
    public String createBook() {
        return "editbook";
    }

    @GetMapping("/books/{id}/edit")
    public String editBook() {
        return "editbook";
    }

    @GetMapping("/books/{id}")
    public String showBook() {
        return "book";
    }

//    @PutMapping("/book")
//    public String updateBook(@Valid @ModelAttribute("book") UpdateBookDto book, BindingResult bindingResult,
//                             Model model) {
//        if (bindingResult.hasErrors()) {
//            var authors = authorService.findAll();
//            var genres = genreService.findAll();
//            model.addAttribute("authors", authors);
//            model.addAttribute("genres", genres);
//            return "editbook";
//        }
//        bookService.update(book);
//        return "redirect:/books";
//    }
//
//    @PostMapping("/book")
//    public String createBook(@Valid @ModelAttribute("book") NewBookDto book, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            var authors = authorService.findAll();
//            var genres = genreService.findAll();
//            model.addAttribute("authors", authors);
//            model.addAttribute("genres", genres);
//            return "editbook";
//        }
//        bookService.create(book);
//        return "redirect:/books";
//    }
}
