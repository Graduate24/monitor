package watchdogagent;

import lombok.extern.slf4j.Slf4j;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.junit.jupiter.api.Test;
import watchdogagent.util.FileUtility;
import watchdogagent.watch.PathIdentity;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ran Zhang
 * @since 2024/3/27
 */
@Slf4j
public class ScheduledTest {
    @Test
    public void test() throws InterruptedException {
        AtomicInteger i = new AtomicInteger();
        Runnable task = () -> System.out.println("test task ---- " + (i.getAndIncrement()));
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

        // Schedule a task that will be executed in 120 sec
        //executor.schedule(task, 120, TimeUnit.SECONDS);

        // Schedule a task that will be first run in 120 sec and each 120sec
        // If an exception occurs then it's task executions are canceled.
        //executor.scheduleAtFixedRate(task, 120, 120, TimeUnit.SECONDS);

        // Schedule a task that will be first run in 120 sec and each 120sec after the last execution
        // If an exception occurs then it's task executions are canceled.
        executor.scheduleWithFixedDelay(task, 10, 10, TimeUnit.SECONDS);
        Thread.sleep(20 * 1000);
        executor.scheduleWithFixedDelay(task, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(50 * 1000);

    }

    @Test
    public void test1() {
        List<String> jspClasses = FileUtility.filterFile("/home/ran/download/apache-tomcat-10.0.4/webapps/jsp-demo/",
                null, null);
        System.out.println(jspClasses);
    }

    private String normalizePattern(final String p) {
        String pattern = p.replace('/', File.separatorChar)
                .replace('\\', File.separatorChar);
        if (pattern.endsWith(File.separator)) {
            pattern += SelectorUtils.DEEP_TREE_MATCH;
        }
        return pattern;
    }

    @Test
    public void test2() {
        PathIdentity pathIdentity = new PathIdentity("/home/ran/download/apache-tomcat-10.0.4/webapps/jsp-demo",
                new String[]{"*.jsp", "a/*.jsp", "**"}, new String[]{""});
        System.out.println(pathIdentity.in("/home/ran/download/apache-tomcat-10.0.4/webapps/jsp-demo/a//b/fe.jsp"));
    }
}
