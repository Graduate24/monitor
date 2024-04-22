package watchdogagent.dispatch.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import watchdogagent.dispatch.Message;

import java.io.IOException;

/**
 * @author Ran Zhang
 * @since 2024/4/10
 */
@Service("addwatchpath")
public class AddWatchPathBroker extends BasicMessageBroker {
    protected Logger logger = LoggerFactory.getLogger(AddWatchPathBroker.class);

    @Override
    public Message handleMessage(Message message, WebSocketSession session) throws IOException {
        logger.info("AddWatchPathBroker handle message");
        //TODO handle add watch path

        return null;
    }
}
