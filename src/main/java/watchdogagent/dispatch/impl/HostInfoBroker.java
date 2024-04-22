package watchdogagent.dispatch.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import watchdogagent.dispatch.Message;
import watchdogagent.monitor.MonitorService;
import watchdogagent.monitor.entity.HostInfo;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Ran Zhang
 * @since 2024/4/2
 */
@Service("hostinfo")
public class HostInfoBroker extends BasicMessageBroker {

    @Resource
    private MonitorService monitorService;

    @Override
    public Message handleMessage(Message message, WebSocketSession session) throws IOException {
        logger.info("HostinfoBroker handle message");
        HostInfo hostInfo = monitorService.hostInfo();
        session.sendMessage(new TextMessage(Message.responseMsg("hostInfo", hostInfo)));
        return null;
    }
}
