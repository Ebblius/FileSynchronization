package com.ebblius;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class IgnoreFileParser {
    private static final Path PROJECT_PATH = Path.of("").toAbsolutePath();
    private final List<Path> paths;

    public IgnoreFileParser(File ignoreFile) {
        this.paths = new ArrayList<>();
        makePaths(ignoreFile.toPath());
    }

    public IgnoreFileParser(Path ignoreFile) {
        this.paths = new ArrayList<>();
        makePaths(ignoreFile);
    }

    private void makePaths(Path ignoreFile) {
        try {
            List<String> lines = Files.readAllLines(ignoreFile, Charset.defaultCharset());
            List<String> filtered = purifyComments(lines).toList();
            Stream<Path> wildcardPaths = toPath(filtered.stream());
            addRelativePaths(wildcardPaths);
        } catch (IOException e) {
            Logger.getInstance().error(e.getMessage());
        }
    }

    private boolean isExcludePath(Path path) {
        return path.startsWith("!");
    }

    private Stream<Path> toPath(Stream<String> lines) {
        return lines.map(Path::of);
    }

    private Stream<String> purifyComments(List<String> lines) {
        return lines.stream().filter(line -> !line.trim().startsWith("#") && !line.trim().isEmpty());
    }

    private void addRelativePaths(Stream<Path> wildcardPaths) {
        PatternMatcher matcher = new PatternMatcher();
        Set<Path> paths = new HashSet<>();
        try (Stream<Path> ctx = Files.walk(PROJECT_PATH, Integer.MAX_VALUE).skip(1L)) {
            List<Path> ctxList = ctx.toList();
            wildcardPaths.forEach(path -> {
                if (isExcludePath(path))
                    matcher.getRelativePaths(path, ctxList).forEach(paths::remove);
                else
                    paths.addAll(matcher.getRelativePaths(path, ctxList));
            });
        } catch (IOException e) {
            Logger.getInstance().error(e.getMessage());
        }
        this.paths.addAll(paths);
    }

    public boolean shouldIgnore(Path file) {
        Objects.requireNonNull(paths);
        return paths.contains(file);
    }

    public List<Path> parse() {
        return Collections.unmodifiableList(paths);
    }

    private static class PatternMatcher {
        private boolean isRoot;
        private boolean isDir;
        private Pattern pattern;

        boolean matches(Path target) {
            target = target.toAbsolutePath();
            if (!target.startsWith(PROJECT_PATH))
                return false;
            target = target.subpath(PROJECT_PATH.getNameCount(), target.getNameCount());
            return pattern.matcher("/" + target).find();
        }

        private Pattern compilePattern(String expr) {
            String regex = expr.replaceAll("(\\[!(.*)])", "[^$2]")
                    .replaceFirst("^!", "")
                    .replaceAll("(\\\\)(.)", "$2")
                    .replaceAll("\\.", "\\\\.")
                    .replaceAll("\\?", ".")
                    .replaceAll("\\*\\*/?", ".*")
                    .replaceAll("(?<!\\.)(\\*)", ".*");
            return Pattern.compile(String.format("%s%s%s", isRoot ? "^" : "", regex, isDir ? "/" : ""));
        }

        List<Path> getRelativePaths(Path wildcardPath, List<Path> paths) {
            isRoot = wildcardPath.toString().startsWith("/");
            isDir = wildcardPath.toString().endsWith("/");
            pattern = compilePattern(wildcardPath.toString());
            return paths.stream().filter(this::matches).toList();
        }
    }

}
