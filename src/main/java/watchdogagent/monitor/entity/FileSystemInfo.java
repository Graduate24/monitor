package watchdogagent.monitor.entity;

import lombok.Data;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Data
@ToString
public class FileSystemInfo {
    private long totalBytes;
    private long usableBytes;
    private String total;
    private String usable;
    private List<FileSystem> fileSystem = new LinkedList<>();

    @Data
    @ToString
    public static class FileSystem {
        private String name;
        private String type;
        private String usable;
        private long usableBytes;
        private String totalSpace;
        private long totalSpaceBytes;
        private String freePercentage;
        private String volume;
        private String logicalVolume;
        private String mount;
    }

}
