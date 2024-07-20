package dev.deyve.bookmarkservice.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record BookmarkError(String message, HttpStatus httpStatus, ZonedDateTime zonedDateTime) {

}
