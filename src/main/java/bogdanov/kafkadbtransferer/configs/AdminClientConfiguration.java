package bogdanov.kafkadbtransferer.configs;

import bogdanov.kafkadbtransferer.database.entities.OriginalRecordEntity;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConditionalOnProperty(
        value = "mode",
        havingValue = "consume")
public class AdminClientConfiguration {

    @Bean
    public AdminClient getAdminClient(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServerConfig) {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
        return AdminClient.create(config);
    }

    @Bean
    public KafkaConsumer<String, OriginalRecordEntity> getKafkaConsumer(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServerConfig,
            @Value("${spring.kafka.consumer.key-deserializer}") String keyDeserializerConfig,
            @Value("${spring.kafka.consumer.value-deserializer}") String valueDeserializerConfig) {

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerConfig);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerConfig);
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerConfig);
        return new KafkaConsumer<>(properties);
    }

}
