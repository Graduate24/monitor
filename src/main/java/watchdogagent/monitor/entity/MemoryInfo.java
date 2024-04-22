package watchdogagent.monitor.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author Ran Zhang
 * @since 2024/4/2
 */
@Data
@ToString
public class MemoryInfo {
    private String available;
    private String total;
    private String swapUsed;
    private String swapTotal;
}
