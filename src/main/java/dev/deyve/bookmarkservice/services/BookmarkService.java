package dev.deyve.bookmarkservice.services;

import dev.deyve.bookmarkservice.dtos.TabsDTO;
import dev.deyve.bookmarkservice.exceptions.BookmarkNotFoundException;
import dev.deyve.bookmarkservice.exceptions.BusinessException;
import dev.deyve.bookmarkservice.models.Bookmark;
import dev.deyve.bookmarkservice.repositories.BookmarkRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Bookmark Service
 */
@Service
public class BookmarkService {

    private static final Logger logger = LogManager.getLogger(BookmarkService.class);

    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    /**
     * Save Tabs
     *
     * @param tabsDTO TabsDTO
     */
    public void saveTabs(TabsDTO tabsDTO) {

        List<Bookmark> bookmarks = Arrays.stream(tabsDTO.getTabs())
                .map(tab -> Bookmark.builder()
                        .title(tab.getTitle())
                        .favIconUrl(tab.getFavIconUrl())
                        .url(tab.getUrl())
                        .build()
                ).toList();

        try {
            bookmarkRepository.saveAll(bookmarks);
            logger.info("Tabs saved!");
        } catch (Exception e) {
            logger.error("BOOKMARK_SERVICE - Error: {}", e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * Find Bookmarks
     *
     * @return List of Bookmark
     */
    public List<Bookmark> findBookmarks() {

        List<Bookmark> bookmarks = bookmarkRepository.findAll();

        logger.debug("BOOKMARK_SERVICE - Bookmarks founded: {} ", bookmarks);

        return bookmarks;
    }

    /**
     * Find Bookmarks with Pageable
     *
     * @param pageable Pageable
     * @return Page of Bookmark
     */
    public Page<Bookmark> findBookmarks(Pageable pageable) {

        Page<Bookmark> bookmarks = bookmarkRepository.findAll(pageable);

        logger.debug("BOOKMARK_SERVICE - Bookmarks founded: {} ", bookmarks);

        return bookmarks;
    }

    /**
     * @param id String
     * @return Bookmark
     * @throws BookmarkNotFoundException Bookmark Not Found Exception
     */
    public Bookmark findBookmark(String id) {

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(id);

        if (optionalBookmark.isPresent()) {
            return optionalBookmark.get();
        } else {
            throw new BookmarkNotFoundException("Bookmark Not Found!");
        }
    }

    /**
     * Delete Bookmark by Id
     *
     * @param id String
     */
    public void deleteBookmark(String id) {

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(id);

        if (optionalBookmark.isPresent()) {
            bookmarkRepository.deleteById(id);
            logger.info("BOOKMARK_SERVICE - Bookmark deleted");
        } else {
            throw new BookmarkNotFoundException("Bookmark Not Found!");
        }
    }
}
