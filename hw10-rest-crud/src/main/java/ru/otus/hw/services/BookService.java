package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.NewBookDto;
import ru.otus.hw.dto.UpdateBookDto;
import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(NewBookDto bookDto);

    BookDto update(UpdateBookDto bookDto);

    void deleteById(long id);
}
