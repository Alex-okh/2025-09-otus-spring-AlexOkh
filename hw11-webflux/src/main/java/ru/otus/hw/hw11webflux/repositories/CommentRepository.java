package ru.otus.hw.hw11webflux.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.hw11webflux.models.Comment;
import java.util.List;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> findByBookId(String id);

    void deleteByBookId(String id);
}
