package com.ebblius;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileCopier {

    void copyFile(Path src, Path dest) throws IOException {
        if (Files.notExists(src))
            throw new IOException("No such file: " + src.toAbsolutePath());

        if (Files.isDirectory(dest)) {
            Files.createDirectories(dest);
            return;
        }

        try (InputStream srcStream = Files.newInputStream(src);
             OutputStream destStream = Files.newOutputStream(dest)) {
            destStream.write(srcStream.readAllBytes());
        }
    }

    void copyFiles(Path[] srcs, Path dest) throws IOException {
        if (Files.notExists(dest))
            Files.createDirectories(dest);

        for (Path src : srcs) {
            Path destFile = dest.resolve(src.getFileName());
            copyFile(src, destFile);
        }
    }
}
