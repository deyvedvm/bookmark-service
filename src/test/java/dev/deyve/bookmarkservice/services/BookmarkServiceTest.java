package dev.deyve.bookmarkservice.services;

import dev.deyve.bookmarkservice.dtos.BookmarkDTO;
import dev.deyve.bookmarkservice.dtos.TabsDTO;
import dev.deyve.bookmarkservice.exceptions.BookmarkNotFoundException;
import dev.deyve.bookmarkservice.exceptions.BusinessException;
import dev.deyve.bookmarkservice.mappers.BookmarkMapper;
import dev.deyve.bookmarkservice.models.Bookmark;
import dev.deyve.bookmarkservice.repositories.BookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.deyve.bookmarkservice.dummies.BookmarkDummy.buildBookmark;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Bookmark Service Test")
class BookmarkServiceTest {

    @Mock
    private BookmarkMapper bookmarkMapper;

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    private final String id = "6287d353bfdfbf082b76187e";
    private final String idFail = "6287d353biff082b76187e";

    @BeforeEach
    void init() {
    }

    @Test
    @DisplayName("Should save tabs from json file")
    void shouldSaveTabsFromJson() {
        Bookmark bookmarkMock = buildBookmark(null, null, null, null);

        List<Bookmark> bookmarkList = List.of(bookmarkMock, buildBookmark(
                "6287d353bfdfbf082b76187f",
                "https://www.youtube.com/s/desktop/2b1b3e1c/img/favicon_32.png",
                "Electron Quick Start",
                "https://www.youtube.com/watch?v=Qg0yYqjwNkI"));

        BookmarkDTO[] bookmarkArrayDTO = bookmarkList.stream().map(bookmark -> new BookmarkDTO(
                null,
                bookmark.getFavIconUrl(),
                bookmark.getTitle(),
                bookmark.getUrl()
        )).toArray(BookmarkDTO[]::new);

        TabsDTO tabsDTO = new TabsDTO(bookmarkArrayDTO);

        when(bookmarkRepository.saveAll(anyList())).thenReturn(bookmarkList);

        bookmarkService.saveTabs(tabsDTO);

        verify(bookmarkRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should throw BusinessException when save tabs")
    void shouldThrowBusinessExceptionWhenSaveTabs() {
        Bookmark bookmarkMock = buildBookmark(
                id,
                "https://github.githubassets.com/favicons/favicon.svg",
                "electron/electron-quick-start: Clone to try a simple Electron app",
                "https://github.com/electron/electron-quick-start"
        );

        List<Bookmark> bookmarkList = List.of(bookmarkMock, buildBookmark(
                "6287d353bfdfbf082b76187f",
                "https://www.youtube.com/s/desktop/2b1b3e1c/img/favicon_32.png",
                "Electron Quick Start",
                "https://www.youtube.com/watch?v=Qg0yYqjwNkI"
        ));

        BookmarkDTO[] bookmarkArrayDTO = bookmarkList.stream().map(bookmark -> new BookmarkDTO(
                null,
                bookmark.getFavIconUrl(),
                bookmark.getTitle(),
                bookmark.getUrl()
        )).toArray(BookmarkDTO[]::new);

        TabsDTO tabsDTO = new TabsDTO(bookmarkArrayDTO);

        when(bookmarkRepository.saveAll(anyList())).thenThrow(new BusinessException("Bookmark error when save tabs"));

        Exception exception = assertThrows(BusinessException.class, () -> bookmarkService.saveTabs(tabsDTO));

        assertEquals("Bookmark error when save tabs", exception.getMessage());
    }

    @Test
    @DisplayName("Should find bookmarks then return page")
    void shouldFindBookmarks() {
        Bookmark bookmarkMock = buildBookmark(
                id,
                "https://github.githubassets.com/favicons/favicon.svg",
                "electron/electron-quick-start: Clone to try a simple Electron app",
                "https://github.com/electron/electron-quick-start"
        );

        List<Bookmark> bookmarkList = List.of(bookmarkMock, buildBookmark(
                "6287d353bfdfbf082b76187f",
                "https://www.youtube.com/s/desktop/2b1b3e1c/img/favicon_32.png",
                "Electron Quick Start",
                "https://www.youtube.com/watch?v=Qg0yYqjwNkI"
        ));

        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Bookmark> bookmarkPageMock = new PageImpl<>(bookmarkList, pageRequest, 2);

        when(bookmarkRepository.findAll(pageRequest)).thenReturn(bookmarkPageMock);

        Page<BookmarkDTO> bookmarkDTOPage = bookmarkService.findBookmarks(pageRequest);

        assertEquals(5, bookmarkDTOPage.getSize());
        assertEquals(2, bookmarkDTOPage.getContent().size());
    }

    @Test
    @DisplayName("Should find a bookmark by Id")
    void shouldFindBookmarkById() {
        Bookmark bookmark = buildBookmark(
                id,
                null,
                "electron/electron-quick-start: Clone to try a simple Electron app",
                null
        );

        BookmarkDTO bookmarkDTO = new BookmarkDTO(
                id,
                null,
                "electron/electron-quick-start: Clone to try a simple Electron app",
                null
        );

        when(bookmarkRepository.findById(id)).thenReturn(Optional.of(bookmark));
        when(bookmarkMapper.toDTO(bookmark)).thenReturn(bookmarkDTO);

        BookmarkDTO bookmarkDTOFound = bookmarkService.findBookmark(id);

        assertEquals(id, bookmarkDTOFound.getId());
        assertEquals("electron/electron-quick-start: Clone to try a simple Electron app", bookmarkDTOFound.getTitle());
    }

    @Test
    @DisplayName("Should throw BookmarkNotFoundException when find a bookmark by Id")
    void shouldThrowBookmarkNotFoundExceptionWhenFindBookmarkById() {
        when(bookmarkRepository.findById(idFail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookmarkNotFoundException.class, () -> bookmarkService.findBookmark(idFail));

        assertEquals("Bookmark Not Found!", exception.getMessage());
    }

    @Test
    @DisplayName("Should update a bookmark by Id")
    void shouldUpdateBookmarkById() {
        String id = UUID.randomUUID().toString();

        Bookmark bookmarkFound = buildBookmark(id, null, "Title", null);
        BookmarkDTO bookmarkDTO = new BookmarkDTO(id, null, "Title", null);
        Bookmark bookmark = buildBookmark(id, null, "Title", null);
        Bookmark bookmarkUpdated = buildBookmark(id, null, "Title updated", null);

        when(bookmarkRepository.findById(id)).thenReturn(Optional.of(bookmarkFound));
        when(bookmarkMapper.toEntity(bookmarkDTO)).thenReturn(bookmark);
        when(bookmarkRepository.save(bookmark)).thenReturn(bookmarkUpdated);
        when(bookmarkMapper.toDTO(bookmarkUpdated)).thenReturn(new BookmarkDTO(id, null, "Title updated", null));

        BookmarkDTO updatedBookmarkDTO = bookmarkService.updateBookmark(id, bookmarkDTO);

        assertEquals("Title updated", updatedBookmarkDTO.getTitle());
    }

    @Test
    @DisplayName("Should throw BookmarkNotFoundException when update a bookmark by Id")
    void shouldThrowBookmarkNotFoundExceptionWhenUpdateBookmarkById() {
        when(bookmarkRepository.findById(idFail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookmarkNotFoundException.class, () -> bookmarkService.updateBookmark(idFail, new BookmarkDTO()));

        assertEquals("Bookmark Not Found!", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete a bookmark by Id")
    void shouldDeleteBookmarkById() {
        when(bookmarkRepository.findById(id)).thenReturn(Optional.of(new Bookmark()));

        bookmarkService.deleteBookmark(id);

        verify(bookmarkRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw BookmarkNotFoundException when delete a bookmark by Id")
    void shouldThrowBookmarkNotFoundExceptionWhenDeleteBookmarkById() {
        when(bookmarkRepository.findById(idFail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BookmarkNotFoundException.class, () -> bookmarkService.deleteBookmark(idFail));

        assertEquals("Bookmark Not Found!", exception.getMessage());
    }
}
