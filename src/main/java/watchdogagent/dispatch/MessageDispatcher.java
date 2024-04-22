package watchdogagent.dispatch;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
public interface MessageDispatcher {

    Message receiveMessage(String message);

    Message handleMessage(Message message, WebSocketSession session) throws IOException;

}
