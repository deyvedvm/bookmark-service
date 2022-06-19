package dev.deyve.bookmarkservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookmarks")
public class Bookmark {

    public Bookmark(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Id
    private String id;

    private String favIconUrl;

    private String title;

    private String url;
}
