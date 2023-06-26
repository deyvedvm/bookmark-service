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

/**
 * Bookmark Service
 */
@Service
public class BookmarkService implements IBookmarkService {

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
    @Override
    public void saveTabs(TabsDTO tabsDTO) {

        List<Bookmark> bookmarks = Arrays.stream(tabsDTO.getTabs())
                .map(bookmarkMapper::toEntity)
                .toList();

        try {
            bookmarkRepository.saveAll(bookmarks);
        } catch (BusinessException e) {
            logger.error("BOOKMARK_SERVICE - Error: {}", e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * Find Bookmarks with Pageable
     *
     * @param pageable Pageable
     * @return Page of Bookmark
     */
    @Override
    public Page<BookmarkDTO> findBookmarks(Pageable pageable) {

        try {
            Page<Bookmark> bookmarkPage = bookmarkRepository.findAll(pageable);

            return bookmarkPage.map(bookmarkMapper::toDTO);
        } catch (BusinessException e) {
            logger.error("BOOKMARK_SERVICE - Error: {}", e.getMessage());
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * Find Bookmark by ID
     *
     * @param id String
     * @return Bookmark
     * @throws BookmarkNotFoundException Bookmark Not Found Exception
     */
    @Override
    public BookmarkDTO findBookmark(String id) {

        Bookmark optionalBookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new BookmarkNotFoundException(BOOKMARK_NOT_FOUND));

        return bookmarkMapper.toDTO(optionalBookmark);
    }

    /**
     * Update Bookmark
     *
     * @param id          String
     * @param bookmarkDTO {@link BookmarkDTO}
     * @return Bookmark
     * @throws BookmarkNotFoundException Bookmark Not Found Exception
     */
    @Override
    public BookmarkDTO updateBookmark(String id, BookmarkDTO bookmarkDTO) {
        bookmarkRepository.findById(id).orElseThrow(() -> new BookmarkNotFoundException(BOOKMARK_NOT_FOUND));

        Bookmark bookmark = bookmarkMapper.toEntity(bookmarkDTO);

        return bookmarkMapper.toDTO(bookmarkRepository.save(bookmark));
    }

    /**
     * Delete Bookmark by Id
     *
     * @param id String
     * @throws BookmarkNotFoundException Bookmark Not Found Exception
     */
    @Override
    public void deleteBookmark(String id) {

        bookmarkRepository.findById(id).orElseThrow(() -> new BookmarkNotFoundException(BOOKMARK_NOT_FOUND));

        bookmarkRepository.deleteById(id);
    }

}
