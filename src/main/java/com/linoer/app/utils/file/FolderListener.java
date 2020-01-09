package com.linoer.app.utils.file;

import com.linoer.app.utils.file.callback.FolderListenerCallBack;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件夹监听
 * 使用方法：
 *      FolderListener.addListener(path, FolderListenerCallBack)
 */
public class FolderListener {
    private static ExecutorService fixedThreadPool = Executors.newCachedThreadPool();
    private WatchService ws;
    private String listenerPath;
    private FolderListenerCallBack callBack;

    private FolderListener(String path, FolderListenerCallBack callBack) {
        try {
            ws = FileSystems.getDefault().newWatchService();
            this.listenerPath = path;
            this.callBack = callBack;
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        fixedThreadPool.execute(new Listener(ws, this.listenerPath, callBack));
    }

    public static void addListener(String path, FolderListenerCallBack callBack) throws IOException {
        FolderListener resourceListener = new FolderListener(path, callBack);
        Path p = Paths.get(path);
        //注册监听事件，文件的修改
        p.register(resourceListener.ws,
                // 文件修改
                StandardWatchEventKinds.ENTRY_MODIFY,
                // 文件删除
                StandardWatchEventKinds.ENTRY_DELETE,
                // 文件创建
                StandardWatchEventKinds.ENTRY_CREATE,
                // 事件丢失
                StandardWatchEventKinds.OVERFLOW
        );
    }
}

class Listener implements Runnable {
    private WatchService service;
    private String rootPath;
    private FolderListenerCallBack callBack;

    Listener(WatchService service, String rootPath, FolderListenerCallBack callBack) {
        this.service = service;
        this.rootPath = rootPath;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        try {
            while (true) {
                WatchKey watchKey = service.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                for (WatchEvent<?> event : watchEvents) {
                    // 根据事件类型采取不同的操作。。。。。。。
                    callBack.callback(event.context().toString());
                }
                watchKey.reset();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                service.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
