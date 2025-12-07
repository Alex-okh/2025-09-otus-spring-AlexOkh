package ru.otus.hw.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.NewCommentDTO;
import ru.otus.hw.dto.UpdateBookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentsService;
import ru.otus.hw.services.GenreService;

@Controller
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    private final CommentsService commentsService;

    private final CommentMapper commentMapper;

    private final AuthorService authorService;

    private final AuthorMapper authorMapper;

    private final GenreService genreService;

    private final GenreMapper genreMapper;

    @GetMapping("/books")
    public String showAllBooks(Model model) {
        var books = bookMapper.bookToDto(bookService.findAll());
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/create")
    public String createBook(Model model) {
        var book = new Book();
        var authors = authorMapper.authorToDto(authorService.findAll());
        var genres = genreMapper.genreToDto(genreService.findAll());
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "editbook";
    }
    @GetMapping("/books/{id}/edit")
    public String editBook(Model model, @PathVariable int id) {
        var book = bookMapper.bookToDto(bookService.findById(id)
                                                   .orElseThrow(() -> new EntityNotFoundException(
                                                           "Book with id %d not found.".formatted(id))));
        var authors = authorMapper.authorToDto(authorService.findAll());
        var genres = genreMapper.genreToDto(genreService.findAll());
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "editbook";
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

    @PostMapping("/book")
    public String saveBook(UpdateBookDTO bookDto) {
        if (bookDto.id() == 0) {
            bookService.create(bookDto.title(), bookDto.authorId(), bookDto.genreIds());
        } else {
            bookService.update(bookDto.id(), bookDto.title(), bookDto.authorId(), bookDto.genreIds());
        }
        return "redirect:/books";
    }

    @DeleteMapping("/books/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @PostMapping("/book/comment")
    public String saveComment(NewCommentDTO comment) {
        commentsService.save(comment);
        return "redirect:/book/"+comment.bookId();
    }



}
