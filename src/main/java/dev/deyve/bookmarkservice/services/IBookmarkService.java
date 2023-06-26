package dev.deyve.bookmarkservice.services;

import dev.deyve.bookmarkservice.dtos.BookmarkDTO;
import dev.deyve.bookmarkservice.dtos.TabsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookmarkService {

    void saveTabs(TabsDTO tabsDTO);

    Page<BookmarkDTO> findBookmarks(Pageable pageable);

    BookmarkDTO findBookmark(String id);

    BookmarkDTO updateBookmark(String id, BookmarkDTO bookmarkDTO);

    void deleteBookmark(String id);
}
