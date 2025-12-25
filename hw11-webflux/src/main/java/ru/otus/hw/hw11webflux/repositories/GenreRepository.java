package ru.otus.hw.hw11webflux.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.hw11webflux.models.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

}
