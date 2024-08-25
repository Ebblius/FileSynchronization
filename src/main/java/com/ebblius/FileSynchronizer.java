package com.ebblius;

import java.io.File;

public class FileSynchronizer {


//    private File sourceDir;
//    private File targetDir;
//    private FileComparator fileComparator;
//    private FileCopier fileCopier;
//    private SyncReport syncReport;
//    private Logger logger;
//    private IgnoreFileParser ignoreFileParser;
//
//    public FileSynchronizer(File sourceDir, File targetDir) {
//        this.sourceDir = sourceDir;
//        this.targetDir = targetDir;
//        this.fileComparator = new FileComparator();
//        this.fileCopier = new FileCopier();
//        this.syncReport = new SyncReport();
//        this.logger = new Logger();
//        this.ignoreFileParser = new IgnoreFileParser(new File(".ignore"));
//    }
//
//    public void startSync() {
//        try {
//            syncDirectory(sourceDir);
//            logger.log("Synchronization completed.");
//        } catch (IOException e) {
//            logger.log("Synchronization failed: " + e.getMessage());
//        }
//    }
//
//    private void syncDirectory(File dir) throws IOException {
//        File[] files = dir.listFiles();
//
//        if (files != null) {
//            for (File file : files) {
//                if (ignoreFileParser.shouldIgnore(file)) {
//                    continue; // Dosya veya klasör .ignore dosyasında tanımlıysa göz ardı et
//                }
//
//                Path relativePath = sourceDir.toPath().relativize(file.toPath());
//                File targetFile = new File(targetDir, relativePath.toString());
//
//                if (file.isDirectory()) {
//                    // Eğer bu bir klasörse, klasörü tekrar senkronize et
//                    if (!targetFile.exists()) {
//                        targetFile.mkdirs();
//                    }
//                    syncDirectory(file);
//                } else {
//                    // Eğer bu bir dosyaysa, karşılaştır ve kopyala
//                    if (!targetFile.exists() || fileComparator.compareFiles(file, targetFile)) {
//                        fileCopier.copyFile(file, targetFile);
//                        syncReport.addEntry("Copied: " + file.getPath());
//                        logger.log("Copied: " + file.getPath());
//                    }
//                }
//            }
//        }
//    }



    void startSynch(){}

    SyncReport generateReport(){return null;}

    void setSourceDirectory(File directory){}

    void setTargetDirectory(File targetDirectory){}






}
