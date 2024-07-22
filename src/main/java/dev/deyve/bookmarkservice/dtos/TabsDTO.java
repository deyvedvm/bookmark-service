package dev.deyve.bookmarkservice.dtos;

public class TabsDTO {

    private BookmarkDTO[] tabs;

    public TabsDTO() {
    }

    public TabsDTO(BookmarkDTO[] tabs) {
        this.tabs = tabs;
    }

    public BookmarkDTO[] getTabs() {
        return tabs;
    }

    public void setTabs(BookmarkDTO[] tabs) {
        this.tabs = tabs;
    }

}
