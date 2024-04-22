package watchdogagent.dispatch;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import watchdogagent.util.JsonUtil;

/**
 * @author Ran Zhang
 * @since 2024/4/15
 */
@Data
@ToString
@Slf4j
public class Message<T> {
    private Integer code;
    private String msg;
    private String action;
    private T data;

    public Message() {
    }

    public Message(String action) {
        this.action = action;
    }

    public Message(String action, T data) {
        this.action = action;
        this.data = data;
    }

    public static <T> String responseMsg(String action, T data) {
        Message<T> message = new Message<>(action, data);
        String res = JsonUtil.toJsonString(message);
        log.info("response msg:{}", res);
        return res;
    }

}
