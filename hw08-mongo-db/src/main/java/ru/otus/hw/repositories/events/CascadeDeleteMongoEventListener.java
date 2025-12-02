package ru.otus.hw.repositories.events;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.CommentRepository;

@Component
@AllArgsConstructor
public class CascadeDeleteMongoEventListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository commentRepository;

    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        var bookId = event.getDocument()
                          .getObjectId("_id")
                          .toString();
        commentRepository.deleteByBookId(bookId);
    }
}
