package watchdogagent.watch;

/**
 * @author Ran Zhang
 * @since 2024/4/16
 */
public interface EventHandler extends Comparable<EventHandler> {
    int order();

    void handleCreate(PathIdentity pathIdentity, String target);

    void handleModify(PathIdentity pathIdentity, String target);

    void handleDelete(PathIdentity pathIdentity, String target);

    void handleOverflow(PathIdentity pathIdentity, String target);

    default void handleEvent(String name, PathIdentity pathIdentity, String target) {
        switch (name) {
            case "ENTRY_CREATE":
                handleCreate(pathIdentity, target);
                break;
            case "ENTRY_MODIFY":
                handleModify(pathIdentity, target);
                break;
            case "ENTRY_DELETE":
                handleDelete(pathIdentity, target);
                break;
            case "OVERFLOW":
                handleOverflow(pathIdentity, target);
                break;
        }
    }
}
