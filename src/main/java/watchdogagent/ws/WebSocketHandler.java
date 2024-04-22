package watchdogagent.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import watchdogagent.dispatch.Message;
import watchdogagent.dispatch.MessageDispatcher;
import watchdogagent.dispatch.impl.WatchConfigBroker;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    protected Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    private SocketConnector connector;

    @Autowired
    private WatchConfigBroker heartbeatMessageBroker;

    @Resource
    private Map<String, MessageDispatcher> messageDispatcher;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Connection has been established with websocket server. {}", session);
        connector.setConnected(true);
        connector.setSession(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Message m = heartbeatMessageBroker.receiveMessage(message.getPayload());
        MessageDispatcher dispatcher = messageDispatcher.get(m.getAction());
        if (dispatcher != null) {
            dispatcher.handleMessage(m, session);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        connector.setConnected(false);
        connector.setSession(null);
        super.afterConnectionClosed(session, status);
        if (status.getCode() == 1006 || status.getCode() == 1011 || status.getCode() == 1012) {
            connector.reconnect();
        }
    }
}
