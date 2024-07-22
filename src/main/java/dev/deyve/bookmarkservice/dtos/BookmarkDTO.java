package dev.deyve.bookmarkservice.dtos;

import java.util.Objects;

public class BookmarkDTO {

    private String id;
    private String favIconUrl;
    private String title;
    private String url;

    public BookmarkDTO() {
    }

    public BookmarkDTO(String id, String favIconUrl, String title, String url) {
        this.id = id;
        this.favIconUrl = favIconUrl;
        this.title = title;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFavIconUrl() {
        return favIconUrl;
    }

    public void setFavIconUrl(String favIconUrl) {
        this.favIconUrl = favIconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkDTO that = (BookmarkDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BookmarkDTO{" +
                "id='" + id + '\'' +
                ", favIconUrl='" + favIconUrl + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
