package dev.deyve.bookmarkservice.services;

import dev.deyve.bookmarkservice.dtos.BookmarkDTO;
import dev.deyve.bookmarkservice.dtos.TabsDTO;
import dev.deyve.bookmarkservice.exceptions.BookmarkNotFoundException;
import dev.deyve.bookmarkservice.exceptions.BusinessException;
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
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    private final String id = "6287d353bfdfbf082b76187e";

    private final String idFail = "6287d353biff082b76187e";

    private final Bookmark bookmark = Bookmark.builder()
            .id(id)
            .favIconUrl("https://github.githubassets.com/favicons/favicon.svg")
            .title("electron/electron-quick-start: Clone to try a simple Electron app")
            .url("https://github.com/electron/electron-quick-start")
            .build();

    private final List<Bookmark> bookmarkList = List.of(bookmark, Bookmark.builder()
            .id("6287d353bfdfbf082b76187f")
            .favIconUrl("https://www.youtube.com/s/desktop/e90e8d8a/img/favicon_32x32.png")
            .title("4 SUPLEMENTOS QUE TODO NATURAL DEVERIA TOMAR! *perder barriga e ganhar massa mais rápido!* - YouTube")
            .url("https://www.youtube.com/watch?v=GecSSh1zKMw")
            .build());

    @BeforeEach
    void init() {
    }

    @Test
    @DisplayName("Should save tabs from json file")
    void shouldSaveTabsFromJson() {
        BookmarkDTO[] bookmarkArrayDTO = bookmarkList.stream().map(bookmark -> {
            return BookmarkDTO.builder()
                    .title(bookmark.getTitle())
                    .url(bookmark.getUrl())
                    .favIconUrl(bookmark.getFavIconUrl())
                    .build();
        }).toList().toArray(new BookmarkDTO[0]);

        TabsDTO tabsDTO = TabsDTO.builder()
                .tabs(bookmarkArrayDTO)
                .build();

        when(bookmarkRepository.saveAll(anyList())).thenReturn(anyList());

        bookmarkService.saveTabs(tabsDTO);

        verify(bookmarkRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should throw BusinessException when save tabs")
    void shouldThrowBusinessExceptionWhenSaveTabs() {
        BookmarkDTO[] bookmarkArrayDTO = bookmarkList.stream().map(bookmark -> {
            return BookmarkDTO.builder()
                    .title(bookmark.getTitle())
                    .url(bookmark.getUrl())
                    .favIconUrl(bookmark.getFavIconUrl())
                    .build();
        }).toList().toArray(new BookmarkDTO[0]);

        TabsDTO tabsDTO = TabsDTO.builder()
                .tabs(bookmarkArrayDTO)
                .build();

        when(bookmarkRepository.saveAll(anyList())).thenThrow(new BusinessException("Bookmark error when save tabs"));

        Exception exception = assertThrows(BusinessException.class, () -> bookmarkService.saveTabs(tabsDTO));

        assertEquals("Bookmark error when save tabs", exception.getMessage());
    }

    @Test
    @DisplayName("Should find bookmarks then return page")
    void shouldFindBookmarks() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Bookmark> bookmarkPageMock = mock(Page.class);

        when(bookmarkRepository.findAll(pageRequest)).thenReturn(bookmarkPageMock);
        when(bookmarkPageMock.getSize()).thenReturn(5);
        when(bookmarkPageMock.getContent()).thenReturn(bookmarkList);

        Page<Bookmark> bookmarkPage = bookmarkService.findBookmarks(pageRequest);

        assertEquals(5, bookmarkPage.getSize());
        assertEquals(2, bookmarkPage.getContent().size());
    }

    @Test
    @DisplayName("Should find a bookmark by Id")
    void shouldFindBookmarkById() {
        when(bookmarkRepository.findById(id)).thenReturn(Optional.ofNullable(bookmark));

        Bookmark bookmarkFound = bookmarkService.findBookmark(id);

        assertEquals(id, bookmarkFound.getId());
        assertEquals("electron/electron-quick-start: Clone to try a simple Electron app", bookmarkFound.getTitle());
    }

    @Test
    @DisplayName("Should throw BookmarkNotFoundException when find a bookmark by Id")
    void shouldThrowBookmarkNotFoundExceptionWhenFindBookmarkById() {
        when(bookmarkRepository.findById(idFail)).thenThrow(new BookmarkNotFoundException("Bookmark Not Found"));

        Exception exception = assertThrows(BookmarkNotFoundException.class, () -> bookmarkService.findBookmark(idFail));

        assertEquals("Bookmark Not Found", exception.getMessage());
    }


    @Test
    @DisplayName("Should delete a bookmark by Id")
    void shouldDeleteBookmarkById() {
        when(bookmarkRepository.findById(id)).thenReturn(Optional.ofNullable(bookmark));

        bookmarkService.deleteBookmark(id);

        verify(bookmarkRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw BookmarkNotFoundException when delete a bookmark by Id")
    void shouldThrowBookmarkNotFoundExceptionWhenDeleteBookmarkById() {
        when(bookmarkRepository.findById(idFail)).thenReturn(Optional.ofNullable(any()));

        try {
            bookmarkService.deleteBookmark(idFail);
        } catch (Exception e) {
            assertEquals("Bookmark Not Found!", e.getMessage());
        }
    }
}