package watchdogagent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import watchdogagent.config.WatchConfig;
import watchdogagent.config.WatchPath;
import watchdogagent.task.ScheduleEventTask;
import watchdogagent.watch.PathIdentity;
import watchdogagent.watch.UploadProcessor;
import watchdogagent.watch.WatchServiceManager;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Ran Zhang
 * @since 2024/4/22
 */
@Component
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private WatchServiceManager watchServiceManager;

    @Autowired
    private WatchPath watchPath;

    @Autowired
    private ScheduleEventTask scheduleEventTask;

    @Autowired
    private UploadProcessor uploadProcessor;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (watchPath.getDelay() == null || watchPath.getDelay() < 1) {
            throw new IllegalArgumentException("delay must >= 1 minute");
        }
        Set<String> watched = new HashSet<>();
        for (WatchConfig watchConfig : watchPath.getList()) {
            if (watched.contains(watchConfig.getDir())) {
                continue;
            }
            watched.add(watchConfig.getDir());

            if (watchConfig.getDir() == null || "/".equals(watchConfig.getDir())) {
                throw new IllegalArgumentException("watch dir too board");
            }
            PathIdentity pathIdentity = new PathIdentity(watchConfig.getDir(), watchConfig.getInclude(), watchConfig.getExclude());
            //TODO auto detect and add tomcat web path
            if (watchConfig.getFirstTry()) {
                uploadProcessor.firstTry(pathIdentity);
            }
            watchServiceManager.watchPath(pathIdentity);
        }
        executor.scheduleWithFixedDelay(scheduleEventTask, watchPath.getDelay(), watchPath.getDelay(), TimeUnit.MINUTES);
    }
}
