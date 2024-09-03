package com.ebblius;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileCopier {

    private final FileComparator fileComparator;

    public FileCopier() {
        this.fileComparator = new FileComparator();
    }

    public void copyFile(Path src, Path dest) throws IOException {
        if (Files.notExists(src))
            throw new IOException("No such file: " + src.toAbsolutePath());

        if (Files.isDirectory(src)) {
            Files.createDirectories(dest);
            return;
        }

        Files.createFile(dest);

        if (fileComparator.compareFiles(src, dest))
            return;

        try (InputStream srcStream = Files.newInputStream(src);
             OutputStream destStream = Files.newOutputStream(dest)) {
            destStream.write(srcStream.readAllBytes());
        }
    }

    public void copyFiles(Path[] srcs, Path dest) throws IOException {
        if (Files.notExists(dest))
            Files.createDirectories(dest);

        for (Path src : srcs) {
            Path destFile = dest.resolve(src.getFileName());
            copyFile(src, destFile);
        }
    }
}
