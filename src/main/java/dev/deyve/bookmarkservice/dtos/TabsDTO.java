package dev.deyve.bookmarkservice.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TabsDTO {

    BookmarkDTO[] tabs;
}
