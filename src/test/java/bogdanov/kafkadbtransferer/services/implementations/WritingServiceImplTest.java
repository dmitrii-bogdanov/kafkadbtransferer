package bogdanov.kafkadbtransferer.services.implementations;

import bogdanov.kafkadbtransferer.testutils.TestUtil;
import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import bogdanov.kafkadbtransferer.services.kafka.interfaces.ProducerService;
import bogdanov.kafkadbtransferer.database.repositories.OriginalRecordRepository;
import bogdanov.kafkadbtransferer.services.interfaces.WritingService;
import bogdanov.kafkadbtransferer.configs.TestProducerImpl;
import bogdanov.kafkadbtransferer.util.MessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(properties = {
        "mode=nothing"
})
@DirtiesContext
@AutoConfigureTestDatabase
class WritingServiceImplTest {

    @Autowired
    private OriginalRecordRepository originalRecordRepository;
    @Autowired
    private MessageBuilder messageBuilder;

    private ProducerService testProducerService = new TestProducerImpl();
    private TestProducerImpl testProducer = (TestProducerImpl) testProducerService;
    private TestUtil testUtil = new TestUtil();
    private WritingService writingService;
    private final int SIZE = 100;
    private final String PREFIX = "NAME_";

    @Value("${spring.kafka.producer.batch-size}")
    private int producerBatchSize;

    @BeforeEach
    private void cleanAndInit() {
        testProducer.clear();
        originalRecordRepository.deleteAll();
        init();
    }

    private void init() {
        originalRecordRepository.saveAll(testUtil.generateEntities(SIZE, PREFIX));
    }

    @Test
    void readAndProduceAll() {
        List<OriginalRecordEntity> entities = originalRecordRepository.findAll();

        writingService = new WritingServiceImpl(originalRecordRepository, testProducerService, messageBuilder);
        testUtil.setObjectProperty(writingService, "batchSize", producerBatchSize);
        writingService.readAndProduceAll();

        List<OriginalRecordEntity> result = testProducer.getList();

        assertEquals(entities.size(), result.size());
        assertTrue(entities.containsAll(result));
    }
}