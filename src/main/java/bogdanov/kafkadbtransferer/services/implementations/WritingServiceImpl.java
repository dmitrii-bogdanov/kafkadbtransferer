package bogdanov.kafkadbtransferer.services.implementations;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import bogdanov.kafkadbtransferer.database.repositories.OriginalRecordRepository;
import bogdanov.kafkadbtransferer.services.interfaces.WritingService;
import bogdanov.kafkadbtransferer.services.kafka.interfaces.ProducerService;
import bogdanov.kafkadbtransferer.util.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Lazy
public class WritingServiceImpl implements WritingService {

    private final OriginalRecordRepository originalRecordRepository;
    private final ProducerService producerService;
    private final MessageBuilder messageBuilder;

    private static final String LOG_HEADER = "Reading from database: ";

    @Value("${spring.kafka.producer.batch-size}")
    private int batchSize;

    @Override
    public void readAndProduceAll() {
        Page<OriginalRecordEntity> entities;
        boolean hasFinished = false;
        int i = 0;
        while (!hasFinished) {
            entities = originalRecordRepository.findAll(PageRequest.of(i++, batchSize));
            log.info(messageBuilder.getMessageForList(LOG_HEADER, entities.getContent()));
            entities.forEach(producerService::produce);
            hasFinished = !entities.hasNext();
        }
    }

}