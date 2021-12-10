package bogdanov.kafkadbtransferer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Lazy
public class ApplicationCloser {

    private final ConfigurableApplicationContext context;
    public void exit(int exitCode) {
		System.exit(SpringApplication.exit(context, () -> exitCode));
    }

}
