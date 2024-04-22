package watchdogagent.dispatch.impl;

import com.google.gson.Gson;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import watchdogagent.dispatch.Message;
import watchdogagent.dispatch.MessageDispatcher;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Data
public abstract class BasicMessageBroker implements MessageDispatcher {

    protected Logger logger = LoggerFactory.getLogger(BasicMessageBroker.class);
    private Gson gson = new Gson();

    @Override
    public Message receiveMessage(String message) {
        logger.info("receive message:{}", message);
        return gson.fromJson(message, Message.class);
    }


}
