package dev.deyve.bookmarkservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class BookmarkServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(BookmarkServiceApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(BookmarkServiceApplication.class, args);

        ConfigurableEnvironment configurableEnvironment = configurableApplicationContext.getEnvironment();

        logger.info("Running Spring Bookmark Service Application!");
        logger.info("Running on java: {}", System.getProperty("java.version"));
        logger.info("Profile: {}", configurableEnvironment.getDefaultProfiles());
    }

}
