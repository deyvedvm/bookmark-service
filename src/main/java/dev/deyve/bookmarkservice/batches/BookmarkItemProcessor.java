package dev.deyve.bookmarkservice.batches;

import dev.deyve.bookmarkservice.models.Bookmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class BookmarkItemProcessor implements ItemProcessor<Bookmark, Bookmark> {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkItemProcessor.class);

    @Override
    public Bookmark process(Bookmark bookmark) {
        final String title = bookmark.getTitle();
        final String url = bookmark.getUrl();

        final Bookmark transformedBookmark = new Bookmark(title, url);

        logger.info("Converting ({}) into ({})", bookmark, transformedBookmark);

        return transformedBookmark;
    }
}
