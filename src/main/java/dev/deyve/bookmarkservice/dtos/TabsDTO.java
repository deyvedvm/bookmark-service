package dev.deyve.bookmarkservice.dtos;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TabsDTO {

    BookmarkDTO[] tabs;
}
