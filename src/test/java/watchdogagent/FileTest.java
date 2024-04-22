package watchdogagent;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import watchdogagent.file.FileService;

import java.io.File;
import java.io.IOException;

/**
 * @author Ran Zhang
 * @since 2024/4/16
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatchdogAgentApplication.class)
@EnableAutoConfiguration
public class FileTest {

    @Autowired
    FileService fileService;

    @Test
    public void test() throws Exception {
        fileService.put("watchdog.jar",
                "/work/codeanalysis/watchdog-agent/target/watchdog-agent-0.0.1-SNAPSHOT.jar");
    }

    @Test
    public void testHash() throws IOException {
        //ef1b9954c8f7349bae272cdde967efa7
        File file = new File("/work/codeanalysis/war/jsp-demo.war");
        HashCode hashCode = Files.asByteSource(file).hash(Hashing.md5());
        System.out.println(hashCode.toString());
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/work/codeanalysis/war/jsp-demo.war");
        HashCode hashCode = Files.asByteSource(file).hash(Hashing.md5());
        System.out.println(hashCode.toString());
    }

}
