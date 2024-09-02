package com.ebblius;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;


public class FileSynchronizer {


    private Path sourceDirectory;  // Kaynak dizin
    private Path targetDirectory;  // Hedef dizin
    private final List<String>  syncLog=new ArrayList<>();

    FileCopier fileCopier=new FileCopier();




    void startSynch() throws IOException{
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
        if (!Files.exists(target)) {
            Files.createDirectories(target);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            for (Path entry : stream) {
                Path targetPath = target.resolve(entry.getFileName());

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

    // Kaynak dizini ayarlar
    public void setSourceDirectory(Path source) {
        this.sourceDirectory = source;
    }

    // Hedef dizini ayarlar
    public void setTargetDirectory(Path target) {
        this.targetDirectory = target;
    }






}
