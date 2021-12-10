package bogdanov.kafkadbtransferer.services.implementations;

import bogdanov.kafkadbtransferer.database.entities.CopiedRecordEntity;
import bogdanov.kafkadbtransferer.database.repositories.CopiedRecordRepository;
import bogdanov.kafkadbtransferer.services.interfaces.CopyingService;
import bogdanov.kafkadbtransferer.util.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Lazy
public class CopyingServiceImpl implements CopyingService {

    private final CopiedRecordRepository copiedRecordRepository;
    private final MessageBuilder messageBuilder;

    private static final String LOG_HEADER = "Saving to database: ";
    private static final String NULL_LIST_MESSAGE = "CopyingService: received list is null";
    private static final String EMPTY_LIST_MESSAGE = "CopyingService: received list is empty";

    @Override
    public void addAll(List<CopiedRecordEntity> entities) {
        checkList(entities);
        try {
            copiedRecordRepository.saveAll(entities);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        log.info(messageBuilder.getMessageForList(LOG_HEADER, entities));
    }

    @Override
    public void cleanTable() {
        copiedRecordRepository.deleteAllInBatch();
        log.info("Destination table was cleaned");
    }

    private void checkList(List<CopiedRecordEntity> entities) {
        if (Objects.isNull(entities)) {
            log.error(NULL_LIST_MESSAGE);
            throw new RuntimeException(NULL_LIST_MESSAGE);
        }
        if (entities.isEmpty()) {
            log.error(EMPTY_LIST_MESSAGE);
            throw new RuntimeException(EMPTY_LIST_MESSAGE);
        }
    }
}
