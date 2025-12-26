package ru.otus.hw.hw11webflux.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.hw11webflux.models.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {


}
