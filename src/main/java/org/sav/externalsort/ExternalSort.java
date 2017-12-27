package org.sav.externalsort;

import org.sav.externalsort.sorters.DefaultSorter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrey.Shilov on 20.10.2017.
 */
public class ExternalSort {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final DefaultSorter DEFAULT_SORTER = new DefaultSorter();

    private static DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final int DEFAULT_TMP_FILE_SIZE_BYTES = 1024*1024*10;


    public static void sort(Path sourceFile, Path resultFile) {
        sort(sourceFile, resultFile, DEFAULT_SORTER);
    }

    public static void sort(Path sourceFile, Path resultFile, Sorter sorter) {
        try {
            Path tmpDir = Files.createTempDirectory(Paths.get(""), "externalsort");
            Date date = new Date();
            System.out.println("Started: " + dateFormat.format(date));
            List<Path> files = sorter.splitAndSort(sourceFile.toRealPath(), tmpDir);
            date = new Date();
            System.out.println("SplittedAndSorted: " + dateFormat.format(date));
            Merger.mergeSortedFiles(files, resultFile.toAbsolutePath());
            date = new Date();
            System.out.println("Merged: " + dateFormat.format(date));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
