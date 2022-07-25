package dev.deyve.bookmarkservice.controllers;

import dev.deyve.bookmarkservice.dtos.BookmarkDTO;
import dev.deyve.bookmarkservice.dtos.TabsDTO;
import dev.deyve.bookmarkservice.models.Bookmark;
import dev.deyve.bookmarkservice.services.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bookmark Controller
 */
@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private static final Logger logger = LogManager.getLogger(BookmarkController.class);

    private final BookmarkService bookmarkService;

    /**
     * Post Tabs
     *
     * @param tabsDTO New File
     */
    @PostMapping
    @RequestMapping("/tabs")
    public ResponseEntity<Void> postTabs(@RequestBody TabsDTO tabsDTO) {

        bookmarkService.saveTabs(tabsDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Get Bookmarks by Page
     *
     * @return List<BookmarkDTO> List of Bookmarks
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getBookmarks(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "5") int size) {

        logger.info("BOOKMARK_SERVICE - Find bookmarks by page: {} and size: {}", page, size);

        Page<Bookmark> bookmarks = bookmarkService.findBookmarks(PageRequest.of(page, size));

        List<BookmarkDTO> bookmarkDTOS = mapToBookmarkDTOS(bookmarks);

        return ResponseEntity.ok().body(getResponseMap(bookmarks, bookmarkDTOS));
    }

    /**
     * Get Bookmark by ID
     *
     * @param id String
     * @return BookmarkDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookmarkDTO> getBookmark(@PathVariable String id) {

        Bookmark bookmark = bookmarkService.findBookmark(id);

        BookmarkDTO bookmarkDTO = BookmarkDTO.builder().build();

        BeanUtils.copyProperties(bookmark, bookmarkDTO);

        return ResponseEntity.ok().body(bookmarkDTO);
    }

    /**
     * Put Bookmark
     *
     * @param id String
     * @return BookmarkDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookmarkDTO> putBookmark(@RequestBody BookmarkDTO bookmarkDTO, @PathVariable String id) {

        Bookmark bookmark = bookmarkService.updateBookmark(id, bookmarkDTO);

        BookmarkDTO bookmarkUpdatedDTO = BookmarkDTO.builder().build();

        BeanUtils.copyProperties(bookmark, bookmarkUpdatedDTO);

        return ResponseEntity.ok().body(bookmarkUpdatedDTO);
    }

    /**
     * Delete Bookmark by Id
     *
     * @param id String
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable String id) {

        bookmarkService.deleteBookmark(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private List<BookmarkDTO> mapToBookmarkDTOS(Page<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> BookmarkDTO.builder()
                        .id(bookmark.getId())
                        .title(bookmark.getTitle())
                        .favIconUrl(bookmark.getFavIconUrl())
                        .url(bookmark.getUrl())
                        .build())
                .toList();
    }

    private Map<String, Object> getResponseMap(Page<Bookmark> bookmarks, List<BookmarkDTO> bookmarkDTOS) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", bookmarkDTOS);
        response.put("currentPage", bookmarks.getNumber());
        response.put("hasNext", bookmarks.hasNext());
        response.put("hasPrevious", bookmarks.hasPrevious());
        response.put("totalElements", bookmarks.getTotalElements());
        response.put("totalPages", bookmarks.getTotalPages());
        response.put("size", bookmarks.getSize());
        return response;
    }
}
