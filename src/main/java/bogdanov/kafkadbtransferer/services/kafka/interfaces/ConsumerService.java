package bogdanov.kafkadbtransferer.services.kafka.interfaces;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.util.List;

public interface ConsumerService {

    void consumeBatch(List<OriginalRecordEntity> entities);

}
