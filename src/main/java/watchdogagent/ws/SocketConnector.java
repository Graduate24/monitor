package watchdogagent.ws;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import watchdogagent.config.AgentConfig;
import watchdogagent.config.MachineCode;

import javax.annotation.Resource;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Configuration
@Data
public class SocketConnector {

    protected Logger logger = LoggerFactory.getLogger(SocketConnector.class);

    @Lazy
    @Resource
    private WebSocketHandler handler;

    @Resource
    private AgentConfig agentConfig;

    private volatile boolean connected;

    private WebSocketConnectionManager manager;

    private WebSocketSession session;

    public void reconnect() {
        manager.stop();
        manager.start();
    }

    @Bean
    public WebSocketConnectionManager wsConnectionManager() {
        MachineCode machineCode = new MachineCode();
        manager = new WebSocketConnectionManager(client(), handler,
                agentConfig.getWebSocketUri() + machineCode.toUriParams());
        manager.setAutoStartup(false);
        manager.start();
        return manager;
    }

    @Bean
    public StandardWebSocketClient client() {
        return new StandardWebSocketClient();
    }


}
