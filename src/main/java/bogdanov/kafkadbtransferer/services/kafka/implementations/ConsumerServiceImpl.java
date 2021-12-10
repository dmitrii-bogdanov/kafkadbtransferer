package bogdanov.kafkadbtransferer.services.kafka.implementations;

import bogdanov.kafkadbtransferer.database.entities.CopiedRecordEntity;
import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import bogdanov.kafkadbtransferer.services.interfaces.CopyingService;
import bogdanov.kafkadbtransferer.services.kafka.interfaces.ConsumerService;
import bogdanov.kafkadbtransferer.util.MessageBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@EnableScheduling
@Primary
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        value = "mode",
        havingValue = "consume")
public class ConsumerServiceImpl implements ConsumerService {

    private final CopyingService copyingService;
    private final MessageBuilder messageBuilder;
    private final  ObjectMapper objectMapper = new ObjectMapper();

//    private final LagAnalyzerService lagAnalyzerService;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private static final String LOG_HEADER = "Consuming messages: ";

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consumeBatch(List<OriginalRecordEntity> entities) {
        log.info(messageBuilder.getMessageForList(LOG_HEADER, entities));
//        try {
//            lagAnalyzerService.analyzeLag(groupId);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        copyingService.addAll(objectMapper.convertValue(entities, new TypeReference<List<CopiedRecordEntity>>() {
        }));
}

    @EventListener(ListenerContainerIdleEvent.class)
    public void eventHandler(ListenerContainerIdleEvent event) {
        log.info(event.getListenerId() + " is idle");
        ((MessageListenerContainer) event.getSource()).stop();
    }

}
