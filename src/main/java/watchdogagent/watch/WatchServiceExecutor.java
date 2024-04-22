package watchdogagent.watch;

import io.methvin.watcher.DirectoryChangeListener;
import io.methvin.watcher.DirectoryWatcher;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Data
public class WatchServiceExecutor implements Runnable {
    protected Logger logger = LoggerFactory.getLogger(WatchServiceExecutor.class);
    final private PathIdentity pathIdentity;
    final private List<EventHandler> eventHandler;
    final private DirectoryWatcher watcher;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private DirectoryChangeListener buildListener() {
        if (CollectionUtils.isEmpty(eventHandler)) {
            return event -> {
            };
        }
        return event -> {
            if (event.isDirectory()) {
                return;
            }
            logger.info("event:{} path:{}", event.eventType().toString(), event.path().getFileName().toString());
            if (event.path().getFileName().toString().endsWith("~")) {
                return;
            }
            if (event.path().getFileName().toString().startsWith(".")) {
                return;
            }
            if (!pathIdentity.in(event.path().toString())) {
                return;
            }
            switch (event.eventType()) {

                case CREATE:
                    eventHandler.forEach(
                            h -> h.handleCreate(pathIdentity, event.path().toString())
                    );
                    break;
                case MODIFY:
                    eventHandler.forEach(
                            h -> h.handleModify(pathIdentity, event.path().toString())
                    );
                    break;
                case DELETE:
                    eventHandler.forEach(
                            h -> h.handleDelete(pathIdentity, event.path().toString())
                    );
                    break;
            }
        };
    }


    private WatchServiceExecutor(PathIdentity pathIdentity, List<EventHandler> handlers) throws IOException {
        this.pathIdentity = pathIdentity;
        Collections.sort(handlers);
        this.eventHandler = handlers;
        Path path = Paths.get(pathIdentity.getNormalizeDirPath());
        logger.info("---- path {}", path.toString());
        this.watcher = DirectoryWatcher.builder()
                .path(path)
                .logger(logger)
                .listener(buildListener())
                .fileHashing(false)
                .build();
    }

    public static WatchServiceExecutor build(PathIdentity pathIdentity, List<EventHandler> handlers) throws IOException {
        return new WatchServiceExecutor(pathIdentity, handlers);
    }

    public void stopWatching() throws IOException {
        watcher.close();
    }

    public CompletableFuture<Void> watch() {
        logger.info("start watch {}", pathIdentity.getDirPath());
        return watcher.watchAsync();
    }

    @Override
    public void run() {
        this.watch();
    }
}
