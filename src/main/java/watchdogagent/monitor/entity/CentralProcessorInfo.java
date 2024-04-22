package watchdogagent.monitor.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author Ran Zhang
 * @since 2024/4/2
 */
@Data
@ToString
public class CentralProcessorInfo {
    private int physicalProcessorCount;
    private int logicalProcessorCount;
    private String identifier;
    private String processorId;
}
