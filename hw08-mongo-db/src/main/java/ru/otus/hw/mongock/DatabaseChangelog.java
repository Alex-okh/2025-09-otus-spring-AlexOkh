package ru.otus.hw.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;
import java.util.List;
import java.util.stream.IntStream;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropDB", author = "alex", runAlways = true)
    public void dropDB(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "alex", runAlways = true)
    public void insertAuthors(MongoDatabase db) {
        var authors = getDbAuthors().stream()
                                    .map(a -> new Document().append("fullName", a.getFullName()))
                                    .toList();
        db.createCollection("authors");
        MongoCollection<Document> collection = db.getCollection("authors");
        collection.insertMany(authors);
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "alex", runAlways = true)
    public void insertGenres(MongoDatabase db) {
        var genres = getDbGenres().stream()
                                  .map(g -> new Document().append("name", g.getName()))
                                  .toList();
        db.createCollection("genres");
        MongoCollection<Document> collection = db.getCollection("genres");
        collection.insertMany(genres);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "alex", runAlways = true)
    public void insertBooks(BookRepository repo, AuthorRepository aRepo, GenreRepository gRepo) {
        var dbAuthors = aRepo.findAll();
        var dbGenres = gRepo.findAll();
        var books = getDbBooks(dbAuthors, dbGenres);

        repo.saveAll(books);
    }

    @ChangeSet(order = "005", id = "addComments", author = "alex", runAlways = true)
    public void addComments(CommentRepository repo, BookRepository bRepo) {
        var books = bRepo.findAll();
        var comments = getDbComments(books);
        repo.saveAll(comments);

    }

    private List<Author> getDbAuthors() {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Author(null, "Author_" + id))
                        .toList();
    }

    private List<Genre> getDbGenres() {
        return IntStream.range(1, 7)
                        .boxed()
                        .map(id -> new Genre(null, "Genre_" + id))
                        .toList();
    }

    private List<Comment> getDbComments(List<Book> books) {
        var comments = IntStream.range(0, 8)
                                .boxed()
                                .map(id -> new Comment("Comment_" + ((id % 3)+1) + "_" + (id+1), books.get(id % 3)))
                                .toList();
        return comments;
    }

    private List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4)
                        .boxed()
                        .map(id -> new Book(null, "BookTitle_" + id, dbAuthors.get(id - 1),
                                            dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)))
                        .toList();
    }

}
