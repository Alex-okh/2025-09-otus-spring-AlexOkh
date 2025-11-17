package ru.otus.hw.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NamedEntityGraph(name = "comment-book", attributeNodes = {
        @NamedAttributeNode(value = "book", subgraph = "book-authors-genres")},
        subgraphs = {
        @NamedSubgraph(name = "book-authors-genres",
                attributeNodes = {
                    @NamedAttributeNode("author"),
                    @NamedAttributeNode("genres")})})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text", nullable = false, unique = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Override
    public String toString() {
        String bookString = book == null ? "null" : book.toString();
        return "Comment{" + "id=" + id + ", text='" + text + '\'' + ", bookID=" + bookString + '}';
    }
}
