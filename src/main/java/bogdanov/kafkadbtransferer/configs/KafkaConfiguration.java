package bogdanov.kafkadbtransferer.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(
        value = "mode",
        havingValue = "produce"
)
public class KafkaConfiguration {

    @Value("${kafka.topic.name}")
    private String topic;

    @Value("${spring.kafka.listener.concurrency}")
    private int partitions;

    @Bean
    public NewTopic topicExample() {
        return TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(1)
                .build();
    }
}
