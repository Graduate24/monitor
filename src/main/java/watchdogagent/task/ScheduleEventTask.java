package watchdogagent.task;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import watchdogagent.config.EventData;
import watchdogagent.dispatch.Message;
import watchdogagent.util.LocalStorage;
import watchdogagent.watch.PathIdentity;
import watchdogagent.ws.SocketConnector;

import java.io.IOException;
import java.util.*;

/**
 * @author Ran Zhang
 * @since 2024/4/27
 */
@Component
@Slf4j
public class ScheduleEventTask implements Runnable {
    @Autowired
    @Qualifier("eventStorage")
    private LocalStorage<PathIdentity, Set<EventData>> storage;

    @Autowired
    @Qualifier("fullBatchStorage")
    private LocalStorage<PathIdentity, Set<EventData>> fullBatchStorage;

    @Autowired
    private SocketConnector connector;

    @Override
    public void run() {
        try {
            if (connector.isConnected()) {
                sendAndClear(storage, "batchEvent");
                sendAndClear(fullBatchStorage, "fullBatchEvent");
            }
        } catch (Exception e) {
            log.info("ScheduleEventTask run error:{}", e.getMessage());
        }

    }

    private void sendAndClear(LocalStorage<PathIdentity, Set<EventData>> storage, String action) throws IOException {
        if (!storage.isEmpty()) {
            for (PathIdentity identity : storage.getStore().keySet()) {
                List<PathEvent> pathEventList = new LinkedList<>();
                Set<EventData> data = storage.getStore().get(identity);
                pathEventList.add(new PathEvent(identity, data));
                connector.getSession().sendMessage(new TextMessage(Message.responseMsg(action, pathEventList)));
            }
            storage.clear();
        }
    }

    @Data
    static class PathEvent {
        private PathIdentity pathIdentity;
        private Set<EventData> eventData;

        public PathEvent() {
        }

        public PathEvent(PathIdentity pathIdentity, Set<EventData> eventData) {
            this.pathIdentity = pathIdentity;
            this.eventData = eventData;
        }
    }
}
