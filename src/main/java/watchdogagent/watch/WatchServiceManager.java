package watchdogagent.watch;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Data
@Component
@Slf4j
public class WatchServiceManager {

    private WatchServiceManager() {
    }

    @Autowired
    private List<EventHandler> eventHandlers;

    @Qualifier("taskExecutor")
    @Autowired
    private Executor executor;

    @Autowired
    private Map<PathIdentity, WatchServiceExecutor> monitorPath;

    public void watchPath(PathIdentity pathIdentity) throws IOException {
        log.info("watch path:{} ,include {},exclude {}", pathIdentity.getDirPath(), pathIdentity.getInclude(),
                pathIdentity.getExclude());
        stopWatch(pathIdentity);
        WatchServiceExecutor watchServiceExecutor = WatchServiceExecutor.build(pathIdentity, eventHandlers);
        executor.execute(watchServiceExecutor);
        monitorPath.put(pathIdentity, watchServiceExecutor);
    }

    public void stopWatch(PathIdentity pathIdentity) throws IOException {
        WatchServiceExecutor ws = monitorPath.get(pathIdentity);
        if (ws != null) {
            ws.stopWatching();
            monitorPath.remove(ws.getPathIdentity());
        }
    }


}
