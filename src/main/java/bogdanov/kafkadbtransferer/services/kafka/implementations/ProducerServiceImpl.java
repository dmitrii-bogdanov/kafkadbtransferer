package bogdanov.kafkadbtransferer.services.kafka.implementations;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import bogdanov.kafkadbtransferer.database.entities.RecordEntity;
import bogdanov.kafkadbtransferer.services.kafka.interfaces.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
@Lazy
public class ProducerServiceImpl implements ProducerService {

    private final KafkaTemplate<String, OriginalRecordEntity> kafkaTemplate;

    private static final String LOG_HEADER = "Producing message: ";

    @Value("${kafka.topic.name}")
    private String topic;

    @Override
    public void produce(OriginalRecordEntity entity) {
        try {
            kafkaTemplate.send(topic, entity);
        } catch (Exception e) {
            log.warn(e.toString());
        }
        log.info(LOG_HEADER + entity);
    }
}
