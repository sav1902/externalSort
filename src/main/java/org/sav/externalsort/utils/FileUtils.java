package org.sav.externalsort.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by Andrey.Shilov on 27.12.2017.
 */
public final class FileUtils {

    private FileUtils() {
        super();
    }

    public static void deleteDirectory(Path path) {
        if (!path.toFile().isDirectory()) {
            return;
        }
        try {
            deleteRecursive(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void deleteRecursive(Path path) throws IOException {
        if (path.toFile().isDirectory()) {
            Files.walkFileTree(path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (path.equals(file)) {
                        return FileVisitResult.CONTINUE;
                    } else if (file.toFile().isDirectory()) {
                        deleteRecursive(path);
                    }

                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.TERMINATE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });

        }

        Files.delete(path);
    }

}
