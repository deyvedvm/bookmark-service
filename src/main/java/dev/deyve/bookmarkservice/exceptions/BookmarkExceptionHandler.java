package dev.deyve.bookmarkservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class BookmarkExceptionHandler {

    @ExceptionHandler(value = {BookmarkNotFoundException.class})
    public ResponseEntity<Object> handleBookmarkNotFoundException(BookmarkNotFoundException notFoundException) {

        HttpStatus badRequest = HttpStatus.NOT_FOUND;

        BookmarkError bookmarkError = new BookmarkError(
                notFoundException.getMessage(),
                badRequest,
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
        );

        return new ResponseEntity<>(bookmarkError, badRequest);
    }
}
