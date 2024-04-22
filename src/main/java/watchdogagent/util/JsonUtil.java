package watchdogagent.util;

import com.google.gson.Gson;

/**
 * @author Ran Zhang
 * @since 2024/3/23
 */
public class JsonUtil {
    private static final Gson gson = new Gson();

    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }

}
