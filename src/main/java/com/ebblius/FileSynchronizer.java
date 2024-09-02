package com.ebblius;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FileSynchronizer {


    private final List<String> syncLog;
    private final FileCopier fileCopier;
    private final IgnoreFileParser ignoreFileParser;
    private final Path sourceDirectory;  // Kaynak dizin
    private final Path targetDirectory; // Hedef dizin

    public FileSynchronizer(Path sourceDirectory, Path targetDirectory, Path ignoreFile) {
        this.syncLog = new ArrayList<>();
        this.fileCopier = new FileCopier();
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        if (ignoreFile == null)
            this.ignoreFileParser = null;
        else
            this.ignoreFileParser = new IgnoreFileParser(ignoreFile);
    }

    public FileSynchronizer(Path sourceDirectory, Path targetDirectory) {
        this(sourceDirectory, targetDirectory, null);
    }

    public void startSynch() throws IOException {
        if (sourceDirectory == null || targetDirectory == null) {
            throw new IllegalStateException("Source and target directories must be set before syncing.");
        }

        Logger.getInstance().info("Starting synchronization...");

        // Örnek senkronizasyon işlemi (Bu kısmı ihtiyaçlara göre genişletebilirsiniz)
        syncDirectory(sourceDirectory, targetDirectory);
        generateReport().printReport();


        Logger.getInstance().info("Synchronization completed.");
    }

    private void syncDirectory(Path source, Path target) throws IOException {
        // Hedef dizin yoksa oluştur
        if (Files.notExists(target)) {
            Files.createDirectories(target);
        }

        Set<Path> ignoredPaths = ignoreFileParser != null ? new HashSet<>(ignoreFileParser.parse()) : Set.of();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            for (Path entry : stream) {

                Path targetPath = target.resolve(entry.getFileName());

                if (ignoredPaths.contains(entry.toAbsolutePath())) {
                    syncLog.add("File ignored: " + entry);
                    continue;
                }

                if (Files.isDirectory(entry)) {
                    syncDirectory(entry, targetPath);  // Rekürsif olarak alt dizinleri senkronize et
                } else {
                    fileCopier.copyFile(entry, targetPath);
                    syncLog.add("File copied: " + entry + " to " + targetPath);
                }
            }
        }
    }

    // Senkronizasyon raporunu oluşturur
    public SyncReport generateReport() {
        SyncReport report = new SyncReport();
        for (String entry : syncLog) {
            report.addEntry(entry);
        }
        return report;
    }

}
