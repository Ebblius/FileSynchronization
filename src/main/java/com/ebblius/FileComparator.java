package com.ebblius;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class FileComparator {

    public boolean compareFiles(Path src, Path dest) throws IOException {

        if (Files.size(src) != Files.size(dest) ||
                !Files.getLastModifiedTime(src).equals(Files.getLastModifiedTime(dest)))
            return false;

        try (Stream<String> srcLineStream = Files.lines(src);
             Stream<String> destLineStream = Files.lines(dest)) {

            Iterator<String> srcLines = srcLineStream.iterator();
            Iterator<String> destLines = destLineStream.iterator();

            while (srcLines.hasNext() || destLines.hasNext()) {
                String srcLine = srcLines.hasNext() ? srcLines.next() : null;
                String destLine = destLines.hasNext() ? destLines.next() : null;

                if (!Objects.equals(srcLine, destLine)) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<String> getDifferences(Path src, Path dest) throws IOException {
        List<String> differences = new ArrayList<>();

        try (Stream<String> srcLineStream = Files.lines(src);
             Stream<String> destLineStream = Files.lines(dest)) {

            Set<String> srcLineSet = srcLineStream.collect(HashSet::new, HashSet::add, HashSet::addAll);
            destLineStream.forEach(
                    line -> srcLineSet.removeIf(
                            srcLine -> Objects.equals(srcLine, line)
                    )
            );

            differences.addAll(srcLineSet);
        }

        return differences.stream().toList();
    }
}