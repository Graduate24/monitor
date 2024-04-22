package watchdogagent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ran Zhang
 * @since 2024/4/22
 */
@Data
public class WatchConfig {

    private String dir;
    private String file;
    private String[] include = new String[]{"**/*.java", "**/*.jsp", "**/*.class", "**/*.properties", "**/*.yml",
            "**/*.xml", "**/*.yaml", "**/*.smap", "**/*.jar", "**/*.war", "**/*.tld"};
    private String[] exclude;
    private Boolean firstTry = false;

}
