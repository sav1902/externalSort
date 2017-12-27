package org.sav.externalsort;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey.Shilov on 25.12.2017.
 */
public interface Sorter {
    Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    int DEFAULT_TMP_FILE_SIZE_BYTES = 1024*1024*100;

    List<Path> splitAndSort(Path sourceFile, Path tmpDir);

    default Path saveToTmpFile(Iterable<String> strings, Path tmpDir, int fileIndex) throws IOException {
        Path tmpFile = Files.createTempFile(tmpDir, "sorted", ".part." + String.valueOf(fileIndex));
        try(BufferedWriter writer = Files.newBufferedWriter(tmpFile, DEFAULT_CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);) {
            for (String string : strings) {
                writer.write(string);
                writer.newLine();
            }
        }

        return tmpFile;
    }

    default Path saveToTmpFile(String[] strings, Path tmpDir, int fileIndex) throws IOException {
        Path tmpFile = Files.createTempFile(tmpDir, "sorted", ".part." + String.valueOf(fileIndex));
        try(BufferedWriter writer = Files.newBufferedWriter(tmpFile, DEFAULT_CHARSET, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);) {
            for (String string : strings) {
                writer.write(string);
                writer.newLine();
            }
        }

        return tmpFile;
    }
}
