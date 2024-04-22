package watchdogagent.util;

import io.methvin.watcher.DirectoryWatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ran Zhang
 * @since 2024/3/28
 */
@Slf4j
public class DirectoryWatchingUtility {

    private final Path directoryToWatch;
    private final DirectoryWatcher watcher;

    public DirectoryWatchingUtility(Path directoryToWatch) throws IOException {
        this.directoryToWatch = directoryToWatch;
        this.watcher = DirectoryWatcher.builder()
                .path(directoryToWatch)// or use paths(directoriesToWatch)
                .logger(log)
                .listener(event -> {
                    switch (event.eventType()) {
                        case CREATE:
                            if (!event.path().getName(event.path().getNameCount() - 1).toString().endsWith("~")) {
                                log.info("event path " + event.path().toString());
                                log.info("event rootpath " + event.rootPath().toString());
                                log.info("is directory " + event.isDirectory());
                            }

                            break;
                        case MODIFY:
                            if (!event.path().getName(event.path().getNameCount() - 1).toString().endsWith("~")) {
                                log.info("event path " + event.path().toString());
                                log.info("event rootpath " + event.rootPath().toString());
                                log.info("is directory " + event.isDirectory());
                            }
                            break;
                        case DELETE:
                            if (!event.path().getName(event.path().getNameCount() - 1).toString().endsWith("~")) {
                                log.info("event path " + event.path().toString());
                                log.info("event rootpath " + event.rootPath().toString());
                                log.info("is directory " + event.isDirectory());
                            }
                            break;
                    }
                })
                .fileHashing(false) // defaults to true
                // .logger(logger) // defaults to LoggerFactory.getLogger(DirectoryWatcher.class)
                // .watchService(watchService) // defaults based on OS to either JVM WatchService or the JNA macOS WatchService
                .build();
    }

    public void stopWatching() throws IOException {
        watcher.close();
    }

    public CompletableFuture<Void> watch() {
        // you can also use watcher.watch() to block the current thread
        return watcher.watchAsync();
    }
}
