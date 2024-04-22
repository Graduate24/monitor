package watchdogagent.config;

import lombok.Data;
import watchdogagent.watch.PathIdentity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ran Zhang
 * @since 2024/8/28
 */
@Data
public class MonitorPath {
    private Set<PathIdentity> pathIdentities = new HashSet<>();

    public void addPath(PathIdentity pathIdentity) {
        pathIdentities.add(pathIdentity);
    }

    public void removePath(PathIdentity pathIdentity) {
        pathIdentities.remove(pathIdentity);
    }


}
