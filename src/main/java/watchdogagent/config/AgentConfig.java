package watchdogagent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Component
@ConfigurationProperties
@Data
public class AgentConfig {
    private String webSocketUri;
}
