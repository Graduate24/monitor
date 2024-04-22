package watchdogagent.config;

import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * @author Ran Zhang
 * @since 2024/3/27
 */
@Data
@ToString
public class EventData {
    private String objectKey;
    private String filePath;
    private Long size;
    private String md5;
    private String monitorDir;
    private String event;

    public EventData() {
    }

    public EventData(String objectKey, String filePath, Long size, String md5, String monitorDir, String event) {
        this.objectKey = objectKey;
        this.filePath = filePath;
        this.size = size;
        this.md5 = md5;
        this.monitorDir = monitorDir;
        this.event = event;
    }

    public EventData(String objectKey, String filePath, Long size, String md5, String monitorDir) {
        this.objectKey = objectKey;
        this.filePath = filePath;
        this.size = size;
        this.md5 = md5;
        this.monitorDir = monitorDir;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        EventData other = (EventData) obj;
        return Objects.equals(filePath, other.filePath);
    }

    @Override
    public int hashCode() {
        return this.filePath.hashCode();
    }

}
