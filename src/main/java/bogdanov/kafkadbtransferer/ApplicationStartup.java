package bogdanov.kafkadbtransferer;

import bogdanov.kafkadbtransferer.services.interfaces.CopyingService;
import bogdanov.kafkadbtransferer.services.interfaces.WritingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartup {

    private final WritingService writingService;
    private final CopyingService copyingService;

    @Value("${mode}")
    private String mode;

    private static final String PRODUCE = "produce";
    private static final String CONSUME = "consume";

    @EventListener(ApplicationStartedEvent.class)
    public void onStartUp() {
        log.warn(String.format("Started task %s",  mode));
        if (Strings.isBlank(mode)) {
            log.error("Please specify --mode parameter with value consume/produce");
            // exit with error code exit(1)
        }

        switch (mode.toLowerCase(Locale.ROOT)) {
            case PRODUCE ->
                writingService.readAndProduceAll();
            case CONSUME ->
                copyingService.cleanTable();
            default -> {
                // print error  "<mode>" is unknown value. Should be consume or produce
                log.error(String.format("'%s' is unknown value. Should be consume or produce", mode));
                // exit (3)
            }
        }
    }
}
