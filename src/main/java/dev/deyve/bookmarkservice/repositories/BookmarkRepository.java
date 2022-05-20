package dev.deyve.bookmarkservice.repositories;

import dev.deyve.bookmarkservice.models.Bookmark;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Bookmark Repository
 */
public interface BookmarkRepository extends MongoRepository<Bookmark, String> {

}
