package dev.deyve.bookmarkservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document(collection = "bookmarks")
public class Bookmark {

    @Id
    private String id;

    private String favIconUrl;

    private String title;

    private String url;
}
