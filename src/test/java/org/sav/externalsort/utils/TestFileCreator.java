package org.sav.externalsort.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Andrey.Shilov
 */
public final class TestFileCreator {
    private static final String STRING_TEMPLATE = "89200000000";

    private TestFileCreator() {
        super();
    }

    public static Path createTestFile(Path path, Charset charset, int linesCount) throws IOException {
        Path result = Paths.get(path.toString(), "test_" + linesCount + ".csv");
        try(BufferedWriter writer = Files.newBufferedWriter(result, charset, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (int i = linesCount; i > 0; i--) {
                String s = String.valueOf(i);
                s = STRING_TEMPLATE.substring(0, STRING_TEMPLATE.length() - s.length()) + s + ","
                        + Math.round(Math.random() * Math.pow(10,6))+",3,999,"
                        + Math.round(Math.random() * Math.pow(10,16))+",999,"
                        + "2016-11-0" + Math.round(Math.random() * 10) + "T00:00:00.000";
                writer.write(s);
                writer.newLine();
            }
        }

        return result;
    }
}
