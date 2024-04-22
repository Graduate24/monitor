package watchdogagent.monitor.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author Ran Zhang
 * @since 2024/4/9
 */
@Data
@ToString
public class OperateSystemInfo {
    private String family;
    private String manufacturer;
    private String version;
    private String codeName;
    private String buildNumber;


}
