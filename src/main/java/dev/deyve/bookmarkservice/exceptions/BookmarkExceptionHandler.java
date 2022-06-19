package dev.deyve.bookmarkservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class BookmarkExceptionHandler {

    @ExceptionHandler(value = {BookmarkNotFoundException.class})
    public ResponseEntity<Object> handleBookmarkNotFoundException(BookmarkNotFoundException notFoundException) {

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        BookmarkError bookmarkError = new BookmarkError(
                notFoundException.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(bookmarkError, badRequest);
    }
}
