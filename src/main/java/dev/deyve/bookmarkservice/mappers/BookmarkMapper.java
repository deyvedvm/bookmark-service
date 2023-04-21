package dev.deyve.bookmarkservice.mappers;

import dev.deyve.bookmarkservice.dtos.BookmarkDTO;
import dev.deyve.bookmarkservice.models.Bookmark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookmarkMapper {

    BookmarkDTO toDTO(Bookmark bookmark);

    Bookmark toEntity(BookmarkDTO bookmarkDTO);
}
