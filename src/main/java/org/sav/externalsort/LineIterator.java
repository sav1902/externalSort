package org.sav.externalsort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Andrey.Shilov
 */
public class LineIterator implements Iterator<String>, AutoCloseable {

    private final BufferedReader reader;
    private String currentLine;
    private String nextLine;
    private boolean lastLineRead = false;

    public LineIterator(Path path) {
        try {
            reader = Files.newBufferedReader(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (null != nextLine) {
            return true;
        } else {
            try {
                nextLine = reader.readLine();
                return (null != nextLine );
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public String next() {
        if (null != nextLine || hasNext()) {
            currentLine = nextLine;
            nextLine = null;
            return currentLine;
        } else {
            throw new NoSuchElementException();
        }
    }

    public String current() {
        return currentLine;
    }

    public boolean isLastLineRead() {
        return lastLineRead;
    }

    public void setLastLineRead(boolean lastLineRead) {
        this.lastLineRead = lastLineRead;
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
