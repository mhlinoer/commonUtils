package com.linoer.app.utils.file;

import com.linoer.app.utils.file.callback.FileWatcherCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 文件监听线程类,监听文件的修改
 */
public class FileWatcherRunnable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FileWatcherRunnable.class);

    private final File file;
    private final CounterGroup counterGroup;

    private long lastChange;
    private FileWatcherCallback callback;
    private String tag;

    /**
     * 构造方法
     *
     * @param file         监听文件
     * @param counterGroup 计数器
     * @param callback     监听回调，需要实现FileWatcherCallback接口
     * @param tag          计数标记
     */
    public FileWatcherRunnable(File file, CounterGroup counterGroup, FileWatcherCallback callback, String tag) {
        super();
        this.file = file;
        this.counterGroup = counterGroup;
        this.lastChange = 0L;
        this.callback = callback;
        this.tag = tag;
    }

    @Override
    public void run() {
        log.debug("Checking file:{} for changes", file);

        counterGroup.incrementAndGet("file.checks");

        long lastModified = file.lastModified();

        if (lastModified > lastChange) {
            log.info("Reloading file:{}", file);

            counterGroup.incrementAndGet(tag);

            lastChange = lastModified;

            try {
                callback.callback();
            } catch (Exception e) {
                log.error("Failed to load configuration data. Exception follows.",
                        e);
            } catch (NoClassDefFoundError e) {
                log.error("Failed to start agent because dependencies were not " +
                        "found in classpath. Error follows.", e);
            } catch (Throwable t) {
                // caught because the caller does not handle or log Throwables
                log.error("Unhandled error", t);
            }
        }
    }
}
