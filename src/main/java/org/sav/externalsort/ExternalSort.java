package org.sav.externalsort;

import org.sav.externalsort.sorters.DefaultSorter;
import org.sav.externalsort.utils.FileUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Andrey.Shilov
 */
public class ExternalSort {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final DefaultSorter DEFAULT_SORTER = new DefaultSorter();

    private static DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static void sort(Path sourceFile, Path resultFile) {
        sort(sourceFile, resultFile, DEFAULT_SORTER);
    }

    public static void sort(Path sourceFile, Path resultFile, Sorter sorter) {
        sort(sourceFile, resultFile, DEFAULT_CHARSET, sorter);
    }

    public static void sort(Path sourceFile, Path resultFile, Charset charset, Sorter sorter) {
            Path tmpDir = createTempDirectory(sourceFile.getFileName().toString()  + ".");
            sort(sourceFile, resultFile, charset, sorter, tmpDir);
            FileUtils.deleteDirectory(tmpDir);
    }

    public static void sort(Path sourceFile, Path resultFile, Charset charset, Sorter sorter, Path tmpDir) {
        try {
            List<Path> files = sorter.splitAndSort(sourceFile.toRealPath(), tmpDir, charset);
            Merger.mergeSortedFiles(files, resultFile.toAbsolutePath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Path createTempDirectory(String prefix) {
        try {
            return Files.createTempDirectory(Paths.get(""), prefix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


}
