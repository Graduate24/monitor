package watchdogagent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Ran Zhang
 * @since 2024/9/10
 */
@Configuration
@ConfigurationProperties(prefix = "watchpath")
@Data
public class WatchPath {
    private List<WatchConfig> list;
    private Integer delay = 1;
}
