package dev.deyve.bookmarkservice.dummies;

import dev.deyve.bookmarkservice.models.Bookmark;

public class BookmarkDummy {

    public static Bookmark.BookmarkBuilder buildBookmark() {

        return Bookmark.builder()
                .id("6287d353bfdfbf082b76187e")
                .favIconUrl("https://github.githubassets.com/favicons/favicon.svg")
                .title("electron/electron-quick-start: Clone to try a simple Electron app")
                .url("https://github.com/electron/electron-quick-start");
    }

}

