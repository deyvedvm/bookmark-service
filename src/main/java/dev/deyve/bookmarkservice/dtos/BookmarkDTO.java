package dev.deyve.bookmarkservice.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {

    String id;

    String favIconUrl;

    String title;

    String url;
}
