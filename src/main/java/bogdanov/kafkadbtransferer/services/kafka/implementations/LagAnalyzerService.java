package bogdanov.kafkadbtransferer.services.kafka.implementations;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.internals.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@EnableScheduling
//@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "mode",
        havingValue = "consume")
public class LagAnalyzerService {

    private final AdminClient adminClient;
    private final KafkaConsumer<String, OriginalRecordEntity> kafkaConsumer;

    @Value("${kafka.topic.name}")
    private String topic;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    private Map<TopicPartition, Long> getConsumerGroupOffsets(String groupId)
            throws ExecutionException, InterruptedException {
        ListConsumerGroupOffsetsResult info = adminClient.listConsumerGroupOffsets(groupId);
        Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap =
                info.partitionsToOffsetAndMetadata().get();

        Map<TopicPartition, Long> groupOffset = new HashMap<>();
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : topicPartitionOffsetAndMetadataMap.entrySet()) {
            TopicPartition key = entry.getKey();
            OffsetAndMetadata metadata = entry.getValue();
            groupOffset.putIfAbsent(new TopicPartition(key.topic(), key.partition()), metadata.offset());
        }
        return groupOffset;
    }

    private Map<TopicPartition, Long> getProducerOffsets(Map<TopicPartition, Long> consumerGroupOffset) {
        List<TopicPartition> topicPartitions = new LinkedList<>();
        for (Map.Entry<TopicPartition, Long> entry : consumerGroupOffset.entrySet()) {
            TopicPartition key = entry.getKey();
            topicPartitions.add(new TopicPartition(key.topic(), key.partition()));
        }
        return kafkaConsumer.endOffsets(topicPartitions);
    }

    private Map<TopicPartition, Long> computeLags(
            Map<TopicPartition, Long> consumerGroupOffsets,
            Map<TopicPartition, Long> producerOffsets) {

        Map<TopicPartition, Long> lags = new HashMap<>();
        for (Map.Entry<TopicPartition, Long> entry : consumerGroupOffsets.entrySet()) {
            Long producerOffset = producerOffsets.get(entry.getKey());
            Long consumerOffset = consumerGroupOffsets.get(entry.getKey());
            long lag = Math.abs(producerOffset - consumerOffset);
            lags.putIfAbsent(entry.getKey(), lag);
        }
        return lags;
    }

    //My
    private Map<TopicPartition, Long> getCurrentLag() throws ExecutionException, InterruptedException {
        ListConsumerGroupOffsetsResult info = adminClient.listConsumerGroupOffsets(groupId);
        Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap =
                info.partitionsToOffsetAndMetadata().get();

        Map<TopicPartition, Long> lags = new HashMap<>();
        for (TopicPartition t : topicPartitionOffsetAndMetadataMap.keySet()) {
            lags.putIfAbsent(t, kafkaConsumer.currentLag(t).getAsLong());
        }
        return lags;
    }

    public void analyzeLag(String groupId) throws ExecutionException, InterruptedException {
        Map<TopicPartition, Long> consumerGroupOffsets = getConsumerGroupOffsets(groupId);
        Map<TopicPartition, Long> producerOffsets = getProducerOffsets(consumerGroupOffsets);
        Map<TopicPartition, Long> computedLags = computeLags(consumerGroupOffsets, producerOffsets);
//        Map<TopicPartition, Long> currentLags = getCurrentLag();

        for (TopicPartition t : computedLags.keySet()) {
            String topic = t.topic();
            int partition = t.partition();
            Long computedLag = computedLags.get(t);
//            Long currentLag = currentLags.get(t);

            log.warn(
                    String.format(
                            "topic = %s, partition = %s: computed: %s, current: %s",
                            topic,
                            partition,
                            computedLag,
                            "-"
                    ));
        }
    }

//    @Scheduled(fixedRate = 100L)
//    public void analyzeLagScheduled() throws ExecutionException, InterruptedException {
//        analyzeLag(groupId);
//    }

}
