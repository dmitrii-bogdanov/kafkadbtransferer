package bogdanov.kafkadbtransferer.services.interfaces;

import bogdanov.kafkadbtransferer.database.entities.CopiedRecordEntity;
import bogdanov.kafkadbtransferer.database.repositories.CopiedRecordRepository;
import bogdanov.kafkadbtransferer.database.repositories.OriginalRecordRepository;
import bogdanov.kafkadbtransferer.testutils.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest(properties = {
        "mode=nothing"
})
@DirtiesContext
@AutoConfigureTestDatabase
class CopyingServiceTest {

    @Autowired
    private CopyingService copyingService;
    @Autowired
    private CopiedRecordRepository copiedRecordRepository;
    @Autowired
    private OriginalRecordRepository originalRecordRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private TestUtil testUtil = new TestUtil();

    private final int SIZE = 100;
    private final String PREFIX = "NAME_";

    @BeforeEach
    private void clear() {
        originalRecordRepository.deleteAll();
        copiedRecordRepository.deleteAll();
    }

    @Test
    void addAll_Valid() {
        originalRecordRepository.saveAll(testUtil.generateEntities(SIZE, PREFIX));
        List<CopiedRecordEntity> entities = objectMapper.convertValue(
                originalRecordRepository.findAll(),
                new TypeReference<List<CopiedRecordEntity>>() {
                });

        copyingService.addAll(entities);

        List<CopiedRecordEntity> result = copiedRecordRepository.findAll();

        assertEquals(entities.size(), result.size());
        assertTrue(entities.containsAll(result));
    }

    @Test
    void addAll_NullEntity() {
        CopiedRecordEntity entity = null;
        List<CopiedRecordEntity> entities = Collections.singletonList(entity);

        assertThrows(Exception.class, () -> copyingService.addAll(entities));
    }

    @Test
    void addAll_NullList() {
        List<CopiedRecordEntity> entities = null;

        assertThrows(RuntimeException.class, () -> copyingService.addAll(entities));
    }

    @Test
    void addAll_EmptyList() {
        List<CopiedRecordEntity> entities = Collections.EMPTY_LIST;

        assertThrows(RuntimeException.class, () -> copyingService.addAll(entities));
    }

    @Test
    void cleanTable() {
        List<CopiedRecordEntity> entities = objectMapper.convertValue(
                testUtil.generateEntities(SIZE, PREFIX),
                new TypeReference<List<CopiedRecordEntity>>() {
                });
        copiedRecordRepository.saveAll(entities);

        assertNotEquals(0,copiedRecordRepository.count());

        copyingService.cleanTable();

        assertEquals(0, copiedRecordRepository.count());
    }

}