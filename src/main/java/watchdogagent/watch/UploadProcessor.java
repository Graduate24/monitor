package watchdogagent.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import watchdogagent.config.EventData;
import watchdogagent.file.FileService;
import watchdogagent.util.FileUtility;
import watchdogagent.util.LocalStorage;
import watchdogagent.util.RandomUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ran Zhang
 * @since 2024/4/16
 */
@Service
public class UploadProcessor implements EventHandler {

    protected Logger logger = LoggerFactory.getLogger(UploadProcessor.class);

    @Autowired
    private FileService fileService;

    @Autowired
    @Qualifier("eventStorage")
    private LocalStorage<PathIdentity, Set<EventData>> storage;

    @Autowired
    @Qualifier("fullBatchStorage")
    private LocalStorage<PathIdentity, Set<EventData>> fullBatchStorage;

    @Override
    public int order() {
        return 0;
    }

    public void firstTry(PathIdentity pathIdentity) {
        List<String> files = FileUtility.filterFile(pathIdentity.getNormalizeDirPath(), pathIdentity.getInclude(),
                pathIdentity.getExclude());
        Set<EventData> dataSet = new HashSet<>();
        files.forEach(f -> {
            try {
                String target = pathIdentity.getNormalizeDirPath() + File.separator + f;
                String md5 = RandomUtil.md5(target);
                String objectKey = RandomUtil.objectKey(md5, Paths.get(target).getFileName().toString());
                fileService.put(objectKey, target);
                long size = RandomUtil.size(target);
                EventData eventData = new EventData(objectKey, target, size, md5, pathIdentity.getNormalizeDirPath(), "init");
                dataSet.add(eventData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        fullBatchStorage.put(pathIdentity, dataSet);
    }


    private void putStorage(PathIdentity pathIdentity, EventData data, String event) {
        data.setEvent(event);
        Set<EventData> eventData = storage.get(pathIdentity);
        if (eventData != null) {
            eventData.remove(data);
            eventData.add(data);
        } else {
            Set<EventData> dataList = new HashSet<>();
            dataList.add(data);
            storage.put(pathIdentity, dataList);
        }

    }

    public EventData newData(String dirPath, String target) throws Exception {
        String md5 = RandomUtil.md5(target);
        Path path = Paths.get(target);
        String fileName = path.getFileName().toString();
        String objectKey = RandomUtil.objectKey(md5, fileName);
        fileService.put(objectKey, target);
        long size = RandomUtil.size(target);
        return new EventData(objectKey, target, size, md5, dirPath);
    }

    @Override
    public void handleCreate(PathIdentity pathIdentity, String target) {
        try {
            logger.info("handle create event, target:{}", target);
            putStorage(pathIdentity, newData(pathIdentity.getNormalizeDirPath(), target), "objectCreate");
        } catch (Exception e) {
            logger.info("handleCreate error.{}", e.getMessage());
        }

    }

    @Override
    public void handleModify(PathIdentity pathIdentity, String target) {
        try {
            logger.info("handle modify event, target:{}", target);
            putStorage(pathIdentity, newData(pathIdentity.getNormalizeDirPath(), target), "objectModify");
        } catch (Exception e) {
            logger.info("handleModify error.{}", e.getMessage());
        }
    }

    @Override
    public void handleDelete(PathIdentity pathIdentity, String target) {
        try {
            EventData data = new EventData();
            data.setFilePath(target);
            data.setMonitorDir(pathIdentity.getNormalizeDirPath());
            putStorage(pathIdentity, data, "objectDelete");
        } catch (Exception e) {
            logger.info("handleDelete error.{}", e.getMessage());
        }
    }

    @Override
    public void handleOverflow(PathIdentity pathIdentity, String target) {

    }

    @Override
    public int compareTo(EventHandler eventHandler) {
        return order() - eventHandler.order();
    }
}
