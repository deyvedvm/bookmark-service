package dev.deyve.bookmarkservice.dummies;

import dev.deyve.bookmarkservice.models.Bookmark;

public class BookmarkDummy {

    public static Bookmark buildBookmark(String id, String favIconUrl, String title, String url) {
        return new Bookmark(
                id != null ? id : "6287d353bfdfbf082b76187e",
                favIconUrl != null ? favIconUrl : "https://github.githubassets.com/favicons/favicon.svg",
                title != null ? title : "electron/electron-quick-start: Clone to try a simple Electron app",
                url != null ? url : "https://github.com/electron/electron-quick-start"
        );
    }
}
