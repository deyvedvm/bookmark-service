package dev.deyve.bookmarkservice.controllers;

import dev.deyve.bookmarkservice.dtos.BookmarkDTO;
import dev.deyve.bookmarkservice.dtos.TabsDTO;
import dev.deyve.bookmarkservice.services.BookmarkService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Bookmark Controller
 */
@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private static final Logger logger = LogManager.getLogger(BookmarkController.class);

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    /**
     * Post Tabs
     *
     * @param tabsDTO New File
     */
    @PostMapping
    @RequestMapping("/tabs")
    public ResponseEntity<Void> postTabs(@RequestBody TabsDTO tabsDTO) {

        logger.info("BOOKMARK_SERVICE - Saving tabs");

        bookmarkService.saveTabs(tabsDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Get Bookmarks by Page
     *
     * @return List<BookmarkDTO> List of Bookmarks
     */
    @GetMapping
    public ResponseEntity<Page<BookmarkDTO>> getBookmarks(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size,
                                                          @RequestParam(defaultValue = "id") String sort) {

        logger.info("BOOKMARK_SERVICE - Finding bookmarks by page {}, size {} and sort {}", page, size, sort);

        return ResponseEntity.ok().body(bookmarkService.findBookmarks(PageRequest.of(page, size, Sort.by(sort))));
    }

    /**
     * Get Bookmark by ID
     *
     * @param id String
     * @return BookmarkDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookmarkDTO> getBookmark(@PathVariable String id) {

        logger.info("BOOKMARK_SERVICE - Finding bookmark by Id: {} ", id);

        return ResponseEntity.ok().body(bookmarkService.findBookmark(id));
    }

    /**
     * Put Bookmark
     *
     * @param id String
     * @return BookmarkDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookmarkDTO> putBookmark(@RequestBody BookmarkDTO bookmarkDTO, @PathVariable String id) {

        logger.info("BOOKMARK_SERVICE - Updating bookmark by Id: {} ", id);

        return ResponseEntity.ok().body(bookmarkService.updateBookmark(id, bookmarkDTO));
    }

    /**
     * Delete Bookmark by Id
     *
     * @param id String
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable String id) {

        logger.info("BOOKMARK_SERVICE - Deleting bookmark by Id: {} ", id);

        bookmarkService.deleteBookmark(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
