package watchdogagent.monitor;

import watchdogagent.monitor.entity.*;

/**
 * @author Ran Zhang
 * @since 2024/4/2
 */
public interface MonitorService {
    ComputerSystemInfo getComputerSystemInfo();

    CentralProcessorInfo getCentralProcessorInfo();

    MemoryInfo getMemoryInfo();

    OperateSystemInfo getOperateSystemInfo();

    NetworkInfo getNetworkInfo();

    FileSystemInfo getFileSystemInfo();

    default HostInfo hostInfo() {
        HostInfo hostInfo = new HostInfo();
        hostInfo.setSystemInfo(getComputerSystemInfo());
        hostInfo.setCentralProcessorInfo(getCentralProcessorInfo());
        hostInfo.setMemoryInfo(getMemoryInfo());
        hostInfo.setOperateSystemInfo(getOperateSystemInfo());
        hostInfo.setNetworkInfo(getNetworkInfo());
        hostInfo.setFileSystemInfo(getFileSystemInfo());
        return hostInfo;
    }

}
