package dev.deyve.bookmarkservice.services;

import dev.deyve.bookmarkservice.dtos.BookmarkDTO;
import dev.deyve.bookmarkservice.dtos.TabsDTO;
import dev.deyve.bookmarkservice.exceptions.BookmarkNotFoundException;
import dev.deyve.bookmarkservice.exceptions.BusinessException;
import dev.deyve.bookmarkservice.mappers.BookmarkMapper;
import dev.deyve.bookmarkservice.models.Bookmark;
import dev.deyve.bookmarkservice.repositories.BookmarkRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 * Bookmark Service
 */
@Service
public class BookmarkService {

    private static final Logger logger = LogManager.getLogger(BookmarkService.class);

    private final String BOOKMARK_NOT_FOUND = "Bookmark Not Found!";

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper bookmarkMapper;

    public BookmarkService(BookmarkRepository bookmarkRepository, BookmarkMapper bookmarkMapper) {
        this.bookmarkRepository = bookmarkRepository;
        this.bookmarkMapper = bookmarkMapper;
    }

    /**
     * Save Tabs
     *
     * @param tabsDTO TabsDTO
     * @throws BusinessException Business Exception
     */
    public void saveTabs(TabsDTO tabsDTO) {

        List<Bookmark> bookmarks = Arrays.stream(tabsDTO.getTabs())
                .map(bookmarkMapper::toEntity)
                .collect(toList());

        try {
            bookmarkRepository.saveAll(bookmarks);
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

        logInfo("BOOKMARK_SERVICE - Bookmarks founded!", bookmarks);

        return bookmarks;
    }

    /**
     * Find Bookmarks with Pageable
     *
     * @param pageable Pageable
     * @return Page of Bookmark
     */
    public Page<BookmarkDTO> findBookmarks(Pageable pageable) {

        Page<Bookmark> bookmarks = bookmarkRepository.findAll(pageable);

        Page<BookmarkDTO> bookmarkDTOS = bookmarks.map(bookmark -> BookmarkDTO.builder()
                .id(bookmark.getId())
                .title(bookmark.getTitle())
                .url(bookmark.getUrl())
                .favIconUrl(bookmark.getFavIconUrl())
                .build());

        logInfo("BOOKMARK_SERVICE - Bookmarks founded!", bookmarkDTOS);

        return bookmarkDTOS;
    }

    /**
     * Find Bookmark by ID
     *
     * @param id String
     * @return Bookmark
     * @throws BookmarkNotFoundException Bookmark Not Found Exception
     */
    public BookmarkDTO findBookmark(String id) {

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(id);

        if (optionalBookmark.isPresent()) {
            logInfo("BOOKMARK_SERVICE - Bookmark founded!", optionalBookmark.get());
            return bookmarkMapper.toDTO(optionalBookmark.get());
        } else {
            throw new BookmarkNotFoundException(BOOKMARK_NOT_FOUND);
        }
    }

    /**
     * Update Bookmark
     *
     * @param id          String
     * @param bookmarkDTO {@link BookmarkDTO}
     * @return Bookmark
     * @throws BookmarkNotFoundException Bookmark Not Found Exception
     */
    public BookmarkDTO updateBookmark(String id, BookmarkDTO bookmarkDTO) {
        Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(id);

        if (optionalBookmark.isPresent()) {
            Bookmark bookmark = bookmarkMapper.toEntity(bookmarkDTO);

            return bookmarkMapper.toDTO(bookmarkRepository.save(bookmark));
        } else {
            throw new BookmarkNotFoundException(BOOKMARK_NOT_FOUND);
        }
    }

    /**
     * Delete Bookmark by Id
     *
     * @param id String
     * @throws BookmarkNotFoundException Bookmark Not Found Exception
     */
    public void deleteBookmark(String id) {

        logger.info("BOOKMARK_SERVICE - Delete bookmark by Id: {} ", id);

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(id);

        if (optionalBookmark.isPresent()) {
            bookmarkRepository.deleteById(id);
            logInfo("BOOKMARK_SERVICE - Bookmark deleted!", optionalBookmark.get());
        } else {
            throw new BookmarkNotFoundException(BOOKMARK_NOT_FOUND);
        }
    }

    private void logInfo(String message, Object o) {
        logger.info("BOOKMARK_SERVICE - {}", message);
        logger.debug("BOOKMARK_SERVICE - {} {}", message, o);
    }
}
