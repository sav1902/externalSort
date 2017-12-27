package org.sav.externalsort;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

/**
 * Created by Andrey.Shilov on 26.12.2017.
 */
public class Merger {

    public static void mergeSortedFiles(List<Path> sortedFiles, Path resultFile) {
        BufferedWriter bufferedWriter = null;
        LineIterator[] lineIterators = null;
        try {
            Objects.requireNonNull(resultFile);
            bufferedWriter = Files.newBufferedWriter(resultFile, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);

            Objects.requireNonNull(sortedFiles);
            lineIterators = createLineIterators(sortedFiles);

            boolean isFirstLine = true;
            String line;
            while ( null != ( line = getNextLine(lineIterators) ) ) {
                if (!isFirstLine) {
                    bufferedWriter.newLine();
                } else {
                    isFirstLine = false;
                }
                bufferedWriter.write(line);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            closeLineIterators(lineIterators);
            closeBufferedWriter(bufferedWriter);
        }
    }

    private static LineIterator[] createLineIterators(List<Path> files) {
        LineIterator[] lineIterators = new LineIterator[files.size()];

        int i = 0;
        for (Path file: files) {
            lineIterators[i] = new LineIterator(file);
            i++;
        }

        return lineIterators;
    }

    private static String getNextLine(LineIterator[] readers) {
        LineIterator iteratorWithMinLine = null;
        for (int i = 0; i < readers.length; i++) {
            if (null == readers[i].current() && readers[i].hasNext()) {
                readers[i].next();
            } else if (readers[i].isLastLineRead()) {
                continue;
            }

            if (null == iteratorWithMinLine) {
                iteratorWithMinLine = readers[i];
            } else if (compareStrings(iteratorWithMinLine.current(), readers[i].current()) >= 0) {
                iteratorWithMinLine = readers[i];
            }
        }

        if (null == iteratorWithMinLine) {
            return null;
        }

        String result = iteratorWithMinLine.current();
        if (iteratorWithMinLine.hasNext()) {
            iteratorWithMinLine.next();
        } else {
            iteratorWithMinLine.setLastLineRead(true);
        }
        return result;
    }

    private static int compareStrings(String string1, String string2) {
        if (null == string2) {
            return -1;
        } else if (null == string1) {
            return 1;
        } else {
            return string1.compareTo(string2);
        }
    }

    private static void closeLineIterators(LineIterator[] lineIterators) {
        if (null == lineIterators || 0 == lineIterators.length) {
            return;
        }
        for (int i = 0; i< lineIterators.length; i++) {
            closeLineIterator(lineIterators[i]);
        }
    }

    private static void closeLineIterator(LineIterator lineIterator) {
        if (lineIterator != null) {
            lineIterator.close();
        }
    }

    private static void closeBufferedWriter(BufferedWriter bufferedWriter) {
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
