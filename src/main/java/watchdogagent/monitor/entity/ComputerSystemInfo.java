package watchdogagent.monitor.entity;

import lombok.Data;
import lombok.ToString;
import watchdogagent.config.MachineCode;

/**
 * @author Ran Zhang
 * @since 2024/4/2
 */
@Data
@ToString
public class ComputerSystemInfo {
    private String manufacturer;
    private String model;
    private String serialNumber;
    private FirmwareInfo firmware;
    private BaseboardInfo baseboard;
    private MachineCode machineCode;

    @Data
    @ToString
    public static class FirmwareInfo {
        String manufacturer;
        String name;
        String description;
        String version;
        String releaseDate;
    }

    @Data
    @ToString
    public static class BaseboardInfo {
        String manufacturer;
        String model;
        String description;
        String version;
        String serialNumber;
    }

}
