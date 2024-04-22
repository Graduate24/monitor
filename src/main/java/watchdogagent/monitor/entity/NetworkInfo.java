package watchdogagent.monitor.entity;

import lombok.Data;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ran Zhang
 * @since 2024/4/9
 */
@Data
@ToString
public class NetworkInfo {
    private String hostname;
    private String domainName;
    private String[] dnsServers;
    private String ipv4DefaultGateway;
    private String ipv6DefaultGateway;
    private List<NetworkInterface> networkInterfaces = new LinkedList<>();

    @Data
    @ToString
    public static class NetworkInterface {
        private String name;
        private String displayName;
        private String macAddr;
        private int mtu;
        private String speed;
        private String[] ipv4addr;
        private String[] ipv6addr;
        private long bytesRecv;
        private long bytesSent;
        private long packetRecv;
        private long packetSent;

    }
}
