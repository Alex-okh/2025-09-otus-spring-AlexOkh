package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepositoryCustom<B, S> {
    List<Book> findAllWithDetails();
    Optional<Book> findByIdWithDetails(String id);
}
