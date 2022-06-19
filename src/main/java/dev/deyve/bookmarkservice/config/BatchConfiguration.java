package dev.deyve.bookmarkservice.config;

import dev.deyve.bookmarkservice.batches.BookmarkItemProcessor;
import dev.deyve.bookmarkservice.listeners.JobCompletionNotificationListener;
import dev.deyve.bookmarkservice.models.Bookmark;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<Bookmark> reader() {
        FlatFileItemReader<Bookmark> flatFileItemReader = new FlatFileItemReaderBuilder<Bookmark>()
                .name("bookmarkItemReader")
                .resource(new ClassPathResource("bookmarks.csv"))
                .delimited()
                .names("title", "url")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Bookmark.class);
                }})
                .build();

        logger.info("FlatFileItemReader created!");

        return flatFileItemReader;
    }

    @Bean
    public BookmarkItemProcessor processor() {
        BookmarkItemProcessor bookmarkItemProcessor = new BookmarkItemProcessor();

        logger.info("BookmarkItemProcessor created!");

        return bookmarkItemProcessor;
    }

    @Bean
    public MongoItemWriter<Bookmark> writer(MongoTemplate mongoTemplate) {
        MongoItemWriter<Bookmark> mongoItemWriter = new MongoItemWriter<>();
        mongoItemWriter.setTemplate(mongoTemplate);
        mongoItemWriter.setCollection("bookmarks");

        logger.info("MongoItemWriter created!");

        return mongoItemWriter;
    }

    @Bean
    public Job importBookmarkJob(JobCompletionNotificationListener listener, Step step1) {
        Job importBookmarkJob = jobBuilderFactory.get("importBookmarkJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();

        logger.info("importBookmarkJob created!");

        return importBookmarkJob;
    }

    @Bean
    public Step step1(MongoItemWriter<Bookmark> writer) {
        TaskletStep step1 = stepBuilderFactory.get("step1")
                .<Bookmark, Bookmark>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();

        logger.info("Step1 created!");

        return step1;
    }

}
