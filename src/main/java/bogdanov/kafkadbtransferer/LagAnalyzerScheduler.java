package bogdanov.kafkadbtransferer;


import bogdanov.kafkadbtransferer.services.kafka.implementations.LagAnalyzerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "mode",
        havingValue = "consume")
public class LagAnalyzerScheduler {

    private final LagAnalyzerService lagAnalyzerService;
    private final ApplicationCloser applicationCloser;

    private List<Long> lags;

    @Scheduled(fixedDelay = 2000L)
    public void checkLag() throws ExecutionException, InterruptedException {
        lags = lagAnalyzerService.analyzeLag();

        int groupLag = 0;
        for (Long lag : lags) {
            groupLag += lag;
        }
        if (groupLag == 0) {
            applicationCloser.exit(0);
        }
    }
}
