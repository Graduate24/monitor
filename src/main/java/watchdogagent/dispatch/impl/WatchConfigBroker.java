package watchdogagent.dispatch.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import watchdogagent.dispatch.Message;

import java.io.IOException;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Service("watchconfig")
public class WatchConfigBroker extends BasicMessageBroker {
    protected Logger logger = LoggerFactory.getLogger(WatchConfigBroker.class);

    @Override
    public Message handleMessage(Message message, WebSocketSession session) throws IOException {
        logger.info("WatchConfigBroker handle message");
        //TODO handle watch path config

        return null;
    }
}
