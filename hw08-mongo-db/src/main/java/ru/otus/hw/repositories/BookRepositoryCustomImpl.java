package ru.otus.hw.repositories;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom<Book, String> {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Book> findAllWithDetails() {
        var aggregation = Aggregation.newAggregation(
                Aggregation.lookup("authors", "author.$id", "_id", "authorDetails"),
                Aggregation.unwind("authorDetails", true),
                Aggregation.lookup("genres", "genres.$id", "_id", "genreDetails"),
                Aggregation.project()
                            .and("_id").as("id")
                            .and("title").as("title")
                            .and("authorDetails").as("author")
                            .and("genreDetails").as("genres"));

        return mongoTemplate.aggregate(aggregation, "books", Book.class).getMappedResults();
    }

    @Override
    public Optional<Book> findByIdWithDetails(String id) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(id)),
                Aggregation.lookup("authors", "author.$id", "_id", "authorDetails"),
                Aggregation.unwind("authorDetails", true),
                Aggregation.lookup("genres", "genres.$id", "_id", "genreDetails"),
                Aggregation.project()
                           .and("_id").as("id")
                           .and("title").as("title")
                           .and("authorDetails").as("author")
                           .and("genreDetails").as("genres"));
        return Optional.ofNullable(
                mongoTemplate.aggregate(aggregation, "books", Book.class)
                             .getUniqueMappedResult());
    }
}
