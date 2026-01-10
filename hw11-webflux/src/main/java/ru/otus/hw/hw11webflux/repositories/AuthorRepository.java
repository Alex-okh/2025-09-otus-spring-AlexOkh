package ru.otus.hw.hw11webflux.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.hw11webflux.models.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
