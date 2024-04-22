package watchdogagent.monitor.entity;

import lombok.Data;

@Data
public class HostInfo {
    private ComputerSystemInfo systemInfo;
    private CentralProcessorInfo centralProcessorInfo;
    private MemoryInfo memoryInfo;
    private OperateSystemInfo operateSystemInfo;
    private NetworkInfo networkInfo;
    private FileSystemInfo fileSystemInfo;
}
