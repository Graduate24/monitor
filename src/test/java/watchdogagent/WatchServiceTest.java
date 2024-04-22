package watchdogagent;

import com.sun.nio.file.SensitivityWatchEventModifier;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;
import watchdogagent.config.EventData;
import watchdogagent.util.DirectoryWatchingUtility;
import watchdogagent.util.JsonUtil;
import watchdogagent.util.RandomUtil;
import watchdogagent.watch.PathIdentity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.sun.jmx.mbeanserver.Util.cast;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Slf4j
public class WatchServiceTest {


    @Test
    public void test() throws Exception {
        //监听目录(项目resource/config目录)，即运行时classes/config目录
        String baseDir = "/work/codeanalysis/watchdog-agent/src/main/resources";
        System.out.println(baseDir);
        //监听文件
        String target_file = "t.txt";
        //构造监听服务
        WatchService watcher = FileSystems.getDefault().newWatchService();
        //监听注册，监听实体的创建、修改、删除事件，并以高频率(每隔2秒一次，默认是10秒)监听
        Paths.get(baseDir).register(watcher,
                new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_DELETE},
                SensitivityWatchEventModifier.MEDIUM);

        while (true) {
            //每隔3秒拉取监听key
            WatchKey key = watcher.poll(3, TimeUnit.SECONDS);  //等待，超时就返回
            //监听key为null,则跳过
            if (key == null) {
                continue;
            }
            //获取监听事件
            for (WatchEvent<?> event : key.pollEvents()) {
                //获取监听事件类型
                WatchEvent.Kind kind = event.kind();
                //异常事件跳过
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }
                //获取监听Path
                Path path = cast(event.context());
                //只关注目标文件
                if (!target_file.equals(path.toString())) {
                    continue;
                }
                //文件删除
                if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.printf("file delete, type:%s  path:%s \n", kind.name(), path);
                    continue;
                }
                //构造完整路径
                Path fullPath = Paths.get(baseDir, path.toString());
                //获取文件
//                File f = fullPath.toFile();
                //读取文件内容
                //输出事件类型、文件路径及内容
                System.out.printf("type:%s  path:%s \n", kind.name(), path);
            }
            //处理监听key后(即处理监听事件后)，监听key需要复位，便于下次监听
            key.reset();
        }
    }

    @Test
    public void test2() throws IOException, InterruptedException {
        WatchService watchService = startWatcher("/work/codeanalysis/thu/sa-monitor/src/main/resources", null);
        Thread.sleep(150000);
        //watchService.close();
    }

    public WatchService startWatcher(String dirPath, String file) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(dirPath);
        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Runnable r = () -> {
            while (true) {
                try {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        log.info(event.context().toString());

                        log.info("{}, {}{}", event.kind().name(), dirPath, file);

                    }
                    boolean reset = key.reset();
                    if (!reset) {
                        log.info("该文件无法重置");
                        break;
                    }
                } catch (ClosedWatchServiceException | InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        };
        new Thread(r).start();
        return watchService;

    }

    @Test
    public void test3() {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        System.out.println(antPathMatcher.match("*.jar", "asfea.jar"));
    }

    private boolean exclude(Path path, BasicFileAttributes fileAttr) {
        Set<String> excludeDir = new HashSet<>();
        excludeDir.add(".git");
        excludeDir.add(".idea");
        excludeDir.add(".github");

        if (!fileAttr.isRegularFile()) {
            return false;
        }

        for (int i = 0; i < path.getNameCount(); i++) {
            String name = path.getName(i).toString();
            if (excludeDir.contains(name)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void test4() {
        try {
            String dirLocation = "/work/codeanalysis/thu/sa-monitor/";
            Files.find(Paths.get(dirLocation),
                    Integer.MAX_VALUE,
                    (filePath, fileAttr) -> {

                        return exclude(filePath, fileAttr);
                    })
                    .forEach(f -> {
                        log.info(String.valueOf(f.toString()));
                    });

        } catch (IOException e) {
            // Error while reading the directory
        }
    }

    @Test
    public void test5() throws IOException, InterruptedException {
        Path path = Paths.get("/work/codeanalysis/thu/sa-monitor/src/main/resources/");
        new DirectoryWatchingUtility(path).watch();
        Thread.sleep(5000000);
    }

    @Test
    public void test6() {
        String path = "/home/ran/download/apache-tomcat-10.0.4/webapps/jsp-demo/WEB-INF/lib/jakarta.servlet-api-5.0.0.jar";
        Path p = Paths.get(path);
        System.out.println(p.getNameCount());
        System.out.println(p.getName(p.getNameCount() - 1));
        System.out.println(p.getFileName().toString());
    }

    @Test
    public void test7() {
        Map<PathIdentity, Set<EventData>> m = new ConcurrentHashMap<>();
        PathIdentity pathIdentity = new PathIdentity("/home/ran/download/apache-tomcat-10.0.4/webapps/jsp-demo", null, null);
        Set<EventData> set = new HashSet<>();
        EventData data = new EventData("fasdf", "asdf", 123l, "1234", "asdf", "asdf");
        set.add(data);
        m.put(pathIdentity, set);
        System.out.println(JsonUtil.toJsonString(m));
    }

    @Test
    public void test8() throws IOException {
        String file = "/home/ran/download/apache-tomcat-10.0.4/conf/context.xml";
        try (InputStream is = Files.newInputStream(Paths.get(file))) {
            String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
            System.out.println(md5);
        }
        System.out.println(RandomUtil.md5(file));
    }
}
