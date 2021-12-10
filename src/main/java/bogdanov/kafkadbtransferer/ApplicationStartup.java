package bogdanov.kafkadbtransferer;

import bogdanov.kafkadbtransferer.services.interfaces.CopyingService;
import bogdanov.kafkadbtransferer.services.interfaces.WritingService;
import bogdanov.kafkadbtransferer.services.kafka.implementations.LagAnalyzerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartup {

    private final WritingService writingService;
    private final CopyingService copyingService;
    private final ApplicationCloser applicationCloser;

    @Value("${mode}")
    private String mode;

    private static final String PRODUCE = "produce";
    private static final String CONSUME = "consume";

    @EventListener(ApplicationStartedEvent.class)
    public void onStartUp() {
        log.info(String.format("Started task %s",  mode));
        if (Strings.isBlank(mode)) {
            log.error("Please specify --mode parameter with value consume/produce");
            applicationCloser.exit(3);
        }

        switch (mode.toLowerCase(Locale.ROOT)) {
            case PRODUCE ->
                writingService.readAndProduceAll();
            case CONSUME ->
                copyingService.cleanTable();
            default -> {
                log.error(String.format("'%s' is unknown value. Should be consume or produce", mode));
                applicationCloser.exit(3);
            }
        }
    }

}
