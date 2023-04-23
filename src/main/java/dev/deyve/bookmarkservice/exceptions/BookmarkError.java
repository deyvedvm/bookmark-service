package dev.deyve.bookmarkservice.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record BookmarkError(String message, HttpStatus httpStatus, LocalDateTime localDateTime) {

}
