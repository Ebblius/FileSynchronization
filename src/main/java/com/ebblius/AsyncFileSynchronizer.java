package com.ebblius;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncFileSynchronizer {
    private final SyncReport syncReport;
    private final Path src;
    private final Path dest;
    private final Set<Path> ignoreFiles;
    private ExecutorService executorService;

    public AsyncFileSynchronizer(Path src, Path dest) {
        this(src, dest, null);
    }

    public AsyncFileSynchronizer(Path src, Path dest, Path ignoreFile) {
        this.src = src;
        this.dest = dest;
        this.syncReport = new SyncReport();
        this.ignoreFiles = new HashSet<>();
        if (ignoreFile != null) {
            this.ignoreFiles.addAll(new IgnoreFileParser(ignoreFile).parse());
        }
    }

    public void startAsyncSync() {
        executorService = Executors.newCachedThreadPool();
        asyncWalk(src, dest);
        executorService.shutdown();
    }

    private void printSyncFinished() {
        Logger.getInstance().info("Sync finished: " + Thread.currentThread().getName());
    }

    private void printSyncStarted() {
        Logger.getInstance().info("Sync started: " + Thread.currentThread().getName());
    }

    private void asyncWalk(Path src, Path dest) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(src)) {
            stream.forEach(path -> {
                if (ignoreFiles.contains(path)) {
                    return;
                }
                if (Files.isDirectory(path)) {
                    Path newDest = dest.resolve(path.getFileName());
                    if (Files.notExists(newDest)) {
                        try {
                            Files.createDirectories(newDest);
                        } catch (IOException e) {
                            Logger.getInstance().error("Error while creating directory: " + newDest);
                        }
                    }
                    asyncWalk(path, newDest);
                } else {
                    executorService.submit(() -> {
                        printSyncStarted();
                        try {
                            new FileCopier().copyFile(path, dest.resolve(path.getFileName()));
                            syncReport.addEntry("File copied: " + path);
                        } catch (IOException e) {
                            syncReport.addEntry("Failed to copy file: " + path);
                        }
                        printSyncFinished();
                    });
                }
            });

        } catch (IOException e) {
            Logger.getInstance().error("Error while walking through directory: " + src);
        }
    }
}