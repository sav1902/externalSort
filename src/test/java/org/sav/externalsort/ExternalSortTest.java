package org.sav.externalsort;

import org.junit.BeforeClass;
import org.junit.Test;
import org.sav.externalsort.sorters.MergeSorter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Andrey.Shilov on 20.10.2017.
 */
public class ExternalSortTest {
    private static DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private static Path tempDir;

    @BeforeClass
    public static void beforeClass() throws IOException {
        //tempDir = Files.createTempDirectory(Paths.get(""), "sortTmp");
    }

    @Test
    public void defaultSorterTest() throws IOException {
        Path sourceFilePath = Paths.get("files/test_10_000_000.csv");
        Path resultFilePath = Paths.get(sourceFilePath.toString() + ".sorted");

        Date start = new Date();
        System.out.println("Started: " +dateFormat.format(start));
        ExternalSort.sort(sourceFilePath, resultFilePath);
        Date end = new Date();
        System.out.println("Sorted: " +dateFormat.format(end));
        System.out.println("Sorted: " +dateFormat.format(end.getTime() - start.getTime()));

        //assertEquals("", sourceFilePath.toFile().length(), resultFilePath.toFile().length());
        assertFileSorted(resultFilePath);
    }

    @Test
    public void mergeSorterTest() throws IOException {
        Path sourceFilePath = Paths.get("files/test_1_000_000.csv");
        Path resultFilePath = Paths.get(sourceFilePath.toString() + ".sorted");

        Date start = new Date();
        System.out.println("Started: " +dateFormat.format(start));
        ExternalSort.sort(sourceFilePath, resultFilePath, new MergeSorter());
        Date end = new Date();
        System.out.println("Sorted: " +dateFormat.format(end));
        System.out.println("Sorted: " +dateFormat.format(end.getTime() - start.getTime()));

        //assertEquals("", sourceFilePath.toFile().length(), resultFilePath.toFile().length());
        assertFileSorted(resultFilePath);
    }

    @Test
    public void googleSortTest() throws IOException {
        Path sourceFilePath = Paths.get("files/test_10_000_000.csv");
        Path resultFilePath = Paths.get(sourceFilePath.toString() + ".sorted.google");
        Date start = new Date();
        System.out.println("Started: " +dateFormat.format(start));
        com.google.code.externalsorting.ExternalSort.sort(sourceFilePath.toFile(), resultFilePath.toFile());
        Date end = new Date();
        System.out.println("Sorted: " +dateFormat.format(end));
        System.out.println("Sorted: " +dateFormat.format(end.getTime() - start.getTime()));

        assertEquals("", sourceFilePath.toFile().length(), resultFilePath.toFile().length());
        assertFileSorted(resultFilePath);
    }

    private void assertFileSorted(Path sortedFilePath) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(sortedFilePath)) {
            String previousLine = null;
            String currentLine;
            while ( (currentLine = reader.readLine()) != null) {
                if (previousLine != null) {
                    assertTrue("", previousLine.compareTo(currentLine) <= 0);
                }

                previousLine = currentLine;
            }
        }
    }
}
