package watchdogagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import watchdogagent.dispatch.Message;
import watchdogagent.monitor.MonitorService;
import watchdogagent.monitor.entity.HostInfo;
import watchdogagent.ws.SocketConnector;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Resource
    private SocketConnector connector;
    @Resource
    private MonitorService monitorService;

    private LocalDateTime lastNotify = LocalDateTime.now();

    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    public void wsConnect() throws IOException {
        if (connector != null && !connector.isConnected()) {
            connector.reconnect();
            log.info("ws reconnecting...");
        }
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lastNotify, now);
        if (duration.toMinutes() >= 5) {
            lastNotify = now;
            log.info("update host info...");
            HostInfo hostInfo = monitorService.hostInfo();
            connector.getSession().sendMessage(new TextMessage(Message.responseMsg("hostInfo", hostInfo)));
        }
    }
}
