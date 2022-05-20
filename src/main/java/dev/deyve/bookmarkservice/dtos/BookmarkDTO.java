package dev.deyve.bookmarkservice.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class BookmarkDTO {

    String id;

    String favIconUrl;

    String title;

    String url;
}
