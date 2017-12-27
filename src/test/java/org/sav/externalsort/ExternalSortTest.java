package org.sav.externalsort;

import org.junit.BeforeClass;
import org.junit.Test;
import org.sav.externalsort.sorters.MergeSorter;
import org.sav.externalsort.utils.TestFileCreator;

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
 * Created by Andrey.Shilov
 */
public class ExternalSortTest {
    private static DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private static int linesCount = 10_000_000;
    private static Path sourceFilePath;
    private static Path testDir;

    @BeforeClass
    public static void beforeClass() throws IOException {
        testDir = Files.createTempDirectory(Paths.get("target"), "sortTest");
        sourceFilePath = TestFileCreator.createTestFile(testDir, ExternalSort.DEFAULT_CHARSET, linesCount);
    }

    @Test
    public void defaultSorterTest() throws IOException {
        System.out.println("defaultSorterTest");
        Path resultFilePath = Paths.get(sourceFilePath.toString() + ".defaultSorterTest.sorted");

        Date start = new Date();
        System.out.println("Started: " + dateFormat.format(start));
        ExternalSort.sort(sourceFilePath, resultFilePath);
        Date end = new Date();
        System.out.println("Sorted: " + dateFormat.format(end));
        System.out.println("Time: " + (end.getTime() - start.getTime()));

        //assertEquals("", sourceFilePath.toFile().length(), resultFilePath.toFile().length());
        assertFileSorted(resultFilePath);
    }

    @Test
    public void mergeSorterTest() throws IOException {
        System.out.println("mergeSorterTest");
        Path resultFilePath = Paths.get(sourceFilePath.toString() + ".mergeSorterTest.sorted");

        Date start = new Date();
        System.out.println("Started: " + dateFormat.format(start));
        ExternalSort.sort(sourceFilePath, resultFilePath, new MergeSorter());
        Date end = new Date();
        System.out.println("Sorted: " + dateFormat.format(end));
        System.out.println("Time: " + (end.getTime() - start.getTime()));

        //assertEquals("", sourceFilePath.toFile().length(), resultFilePath.toFile().length());
        assertFileSorted(resultFilePath);
    }

    @Test
    public void googleSortTest() throws IOException {
        System.out.println("googleSortTest");
        Path resultFilePath = Paths.get(sourceFilePath.toString() + ".googleSortTest.sorted");
        Date start = new Date();
        System.out.println("Started: " +dateFormat.format(start));
        com.google.code.externalsorting.ExternalSort.sort(sourceFilePath.toFile(), resultFilePath.toFile());
        Date end = new Date();
        System.out.println("Sorted: " + dateFormat.format(end));
        System.out.println("Time: " + (end.getTime() - start.getTime()));

        //assertEquals("", sourceFilePath.toFile().length(), resultFilePath.toFile().length());
        assertFileSorted(resultFilePath);
    }

    private void assertFileSorted(Path sortedFilePath) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(sortedFilePath)) {
            String previousLine = null;
            String currentLine;
            int lines = 0;
            while ( (currentLine = reader.readLine()) != null) {
                lines++;
                if (null != previousLine) {
                    assertTrue("", 0 >= previousLine.compareTo(currentLine));
                }

                previousLine = currentLine;
            }
            assertEquals("", linesCount, lines);
        }
    }
}
