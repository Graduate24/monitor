package watchdogagent.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import watchdogagent.util.LocalStorage;
import watchdogagent.watch.PathIdentity;
import watchdogagent.watch.WatchServiceExecutor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ran Zhang
 * @since 2024/8/27
 */
@Configuration
@Slf4j
public class MonitorSetup {
    @Bean
    public LocalStorage<PathIdentity, Set<EventData>> eventStorage() {
        return LocalStorage.newStorage();
    }

    @Bean
    public LocalStorage<PathIdentity, Set<EventData>> fullBatchStorage() {
        return LocalStorage.newStorage();
    }

    @Bean
    public Map<PathIdentity, WatchServiceExecutor> monitorPath() {
        return new ConcurrentHashMap<>();
    }


}
