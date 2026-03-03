package ru.otus.hw.exceptions;

public class BookNotFoundException extends EntityNotFoundException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
