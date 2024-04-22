package watchdogagent.monitor.impl;

import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import watchdogagent.config.MachineCode;
import watchdogagent.monitor.MonitorService;
import watchdogagent.monitor.entity.*;

/**
 * @author Ran Zhang
 * @since 2024/4/2
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    SystemInfo si = new SystemInfo();
    HardwareAbstractionLayer hal = si.getHardware();
    OperatingSystem os = si.getOperatingSystem();


    @Override
    public ComputerSystemInfo getComputerSystemInfo() {
        ComputerSystem computerSystem = hal.getComputerSystem();

        ComputerSystemInfo computerSystemInfo = new ComputerSystemInfo();
        computerSystemInfo.setManufacturer(computerSystem.getManufacturer());
        computerSystemInfo.setModel(computerSystem.getModel());
        computerSystemInfo.setSerialNumber(computerSystem.getSerialNumber());
        computerSystemInfo.setMachineCode(new MachineCode());

        ComputerSystemInfo.FirmwareInfo firmwareInfo = new ComputerSystemInfo.FirmwareInfo();
        final Firmware firmware = computerSystem.getFirmware();
        firmwareInfo.setManufacturer(firmware.getManufacturer());
        firmwareInfo.setName(firmware.getName());
        firmwareInfo.setDescription(firmware.getDescription());
        firmwareInfo.setVersion(firmware.getVersion());
        firmwareInfo.setReleaseDate((firmware.getReleaseDate() == null ? "unknown"
                : firmware.getReleaseDate() == null ? "unknown" : FormatUtil.formatDate(firmware.getReleaseDate())));
        computerSystemInfo.setFirmware(firmwareInfo);

        ComputerSystemInfo.BaseboardInfo baseboardInfo = new ComputerSystemInfo.BaseboardInfo();
        final Baseboard baseboard = computerSystem.getBaseboard();
        baseboardInfo.setManufacturer(baseboard.getManufacturer());
        baseboardInfo.setModel(baseboard.getModel());
        baseboardInfo.setVersion(baseboard.getVersion());
        baseboardInfo.setSerialNumber(baseboard.getSerialNumber());
        computerSystemInfo.setBaseboard(baseboardInfo);

        return computerSystemInfo;
    }


    @Override
    public CentralProcessorInfo getCentralProcessorInfo() {
        CentralProcessor processor = hal.getProcessor();
        CentralProcessorInfo centralProcessorInfo = new CentralProcessorInfo();
        centralProcessorInfo.setPhysicalProcessorCount(processor.getPhysicalProcessorCount());
        centralProcessorInfo.setLogicalProcessorCount(processor.getLogicalProcessorCount());
        centralProcessorInfo.setIdentifier(processor.getIdentifier());
        centralProcessorInfo.setProcessorId(processor.getProcessorID());
        return centralProcessorInfo;
    }

    @Override
    public MemoryInfo getMemoryInfo() {
        GlobalMemory memory = hal.getMemory();
        MemoryInfo memoryInfo = new MemoryInfo();
        memoryInfo.setAvailable(FormatUtil.formatBytes(memory.getAvailable()));
        memoryInfo.setTotal(FormatUtil.formatBytes(memory.getTotal()));
        memoryInfo.setSwapUsed(FormatUtil.formatBytes(memory.getSwapUsed()));
        memoryInfo.setSwapTotal(FormatUtil.formatBytes(memory.getSwapTotal()));
        return memoryInfo;
    }

    @Override
    public OperateSystemInfo getOperateSystemInfo() {
        OperateSystemInfo operateSystemInfo = new OperateSystemInfo();
        operateSystemInfo.setVersion(os.getVersion().getVersion());
        operateSystemInfo.setBuildNumber(os.getVersion().getBuildNumber());
        operateSystemInfo.setCodeName(os.getVersion().getCodeName());
        operateSystemInfo.setFamily(os.getFamily());
        operateSystemInfo.setManufacturer(os.getManufacturer());
        return operateSystemInfo;
    }

    @Override
    public NetworkInfo getNetworkInfo() {
        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setHostname(os.getNetworkParams().getHostName());
        networkInfo.setDomainName(os.getNetworkParams().getDomainName());
        networkInfo.setDnsServers(os.getNetworkParams().getDnsServers());
        networkInfo.setIpv4DefaultGateway(os.getNetworkParams().getIpv4DefaultGateway());
        networkInfo.setIpv6DefaultGateway(os.getNetworkParams().getIpv6DefaultGateway());


        NetworkIF[] networkIFs = hal.getNetworkIFs();
        for (NetworkIF net : networkIFs) {
            NetworkInfo.NetworkInterface networkInterface = new NetworkInfo.NetworkInterface();
            networkInterface.setName(net.getName());
            networkInterface.setDisplayName(net.getDisplayName());
            networkInterface.setMtu(net.getMTU());
            networkInterface.setSpeed(FormatUtil.formatValue(net.getSpeed(), "bps"));
            networkInterface.setIpv4addr(net.getIPv4addr());
            networkInterface.setIpv6addr(net.getIPv6addr());
            networkInterface.setBytesRecv(net.getBytesRecv());
            networkInterface.setBytesSent(net.getBytesSent());
            networkInterface.setPacketRecv(net.getPacketsRecv());
            networkInterface.setPacketSent(net.getPacketsSent());
            networkInfo.getNetworkInterfaces().add(networkInterface);
        }

        return networkInfo;
    }

    @Override
    public FileSystemInfo getFileSystemInfo() {
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        FileSystemInfo fileSystemInfo = new FileSystemInfo();
        long tu = 0l;
        long ta = 0l;
        for (OSFileStore fs : fsArray) {
            long usable = fs.getUsableSpace();
            tu += usable;
            long total = fs.getTotalSpace();
            ta += total;
            FileSystemInfo.FileSystem f = new FileSystemInfo.FileSystem();
            f.setName(fs.getName());
            f.setType(fs.getType());
            f.setUsableBytes(usable);
            f.setUsable(FormatUtil.formatBytes(usable));
            f.setTotalSpaceBytes(total);
            f.setTotalSpace(FormatUtil.formatBytes(total));
            f.setFreePercentage(100d * usable / total + "");
            f.setVolume(fs.getVolume());
            f.setLogicalVolume(fs.getLogicalVolume());
            f.setMount(fs.getMount());

            fileSystemInfo.getFileSystem().add(f);
        }
        fileSystemInfo.setTotalBytes(ta);
        fileSystemInfo.setUsableBytes(tu);
        fileSystemInfo.setTotal(FormatUtil.formatBytes(ta));
        fileSystemInfo.setUsable(FormatUtil.formatBytes(tu));
        return fileSystemInfo;
    }
}
