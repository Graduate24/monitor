package watchdogagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@SpringBootApplication
@EnableScheduling
public class WatchdogAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatchdogAgentApplication.class, args);
    }


}
