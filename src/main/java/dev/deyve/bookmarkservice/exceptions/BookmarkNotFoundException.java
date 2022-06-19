package dev.deyve.bookmarkservice.exceptions;

public class BookmarkNotFoundException extends RuntimeException {

    public BookmarkNotFoundException(String message) {
        super(message);
    }

    public BookmarkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
