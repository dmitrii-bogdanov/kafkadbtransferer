package bogdanov.kafkadbtransferer;

import bogdanov.kafkadbtransferer.database.entities.CopiedRecordEntity;
import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import bogdanov.kafkadbtransferer.database.repositories.CopiedRecordRepository;
import bogdanov.kafkadbtransferer.database.repositories.OriginalRecordRepository;
import bogdanov.kafkadbtransferer.services.implementations.WritingServiceImpl;
import bogdanov.kafkadbtransferer.services.interfaces.WritingService;
import bogdanov.kafkadbtransferer.services.kafka.interfaces.ProducerService;
import bogdanov.kafkadbtransferer.configs.TestKafkaConfiguration;
import bogdanov.kafkadbtransferer.testutils.TestUtil;
import bogdanov.kafkadbtransferer.util.MessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(properties = {
        "mode=consume",
        "spring.kafka.listener.idle-event-interval=30000",
        "kafka.topic.name=test_messages",
        "spring.kafka.consumer.group-id=test_message_group_id"
})
@Import(TestKafkaConfiguration.class)
@DirtiesContext
@AutoConfigureTestDatabase
class AppTest {

    @Autowired
    private CopiedRecordRepository copiedRecordRepository;
    @Autowired
    private OriginalRecordRepository originalRecordRepository;
    @Autowired
    private ProducerService producerService;
    @Autowired
    private MessageBuilder messageBuilder;

    private TestUtil testUtil = new TestUtil();

    private WritingService writingService;

    private final int SIZE = 128;
    private final String PREFIX = "NAME_";

    @Value("${spring.kafka.producer.batch-size}")
    private int producerBatchSize;

    @Value("${spring.kafka.listener.idle-event-interval}")
    private int idleInterval;

    @BeforeEach
    private void clearAndInit() {
        copiedRecordRepository.deleteAll();
        originalRecordRepository.deleteAll();
        init();
    }

    private void init() {
        originalRecordRepository.saveAll(testUtil.generateEntities(SIZE, PREFIX));
    }

    private boolean equals(OriginalRecordEntity original, CopiedRecordEntity copied) {
        return (original.getId() == copied.getId())
                && (original.getName().equals(copied.getName()))
                && original.getTimestamp().equals(copied.getTimestamp());
    }

    @Test
    void produceAndCopyAll() throws InterruptedException {
        List<OriginalRecordEntity> list = originalRecordRepository.findAll();

        writingService = new WritingServiceImpl(originalRecordRepository, producerService, messageBuilder);
        testUtil.setObjectProperty(writingService, "batchSize", producerBatchSize);
        writingService.readAndProduceAll();

        List<CopiedRecordEntity> result = new LinkedList<>();
        int i = 0;
        while ((i < (idleInterval / 1000)) && (result.size() < list.size())) {
            i++;
            sleep(1000);
            result = copiedRecordRepository.findAll();
        }

        assertEquals(list.size(), result.size());
        for (i = 0; i < list.size(); i++) {
            assertTrue(equals(list.get(i), result.get(i)));
        }
    }

}