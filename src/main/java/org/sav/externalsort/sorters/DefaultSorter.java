package org.sav.externalsort.sorters;

import org.sav.externalsort.Sorter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Andrey.Shilov
 */
public class DefaultSorter implements Sorter {

    @Override
    public List<Path> splitAndSort(Path sourceFile, Path tmpDir, Charset charset) {
        List<Path> files = new LinkedList<>();
        try(BufferedReader reader = Files.newBufferedReader(sourceFile)) {
            long length = 0;
            ArrayList<String> arrayList = new ArrayList<>();
            String line;
            while ( (line = reader.readLine()) != null) {
                length += line.length();
                if (length < DEFAULT_TMP_FILE_SIZE_BYTES) {
                    arrayList.add(line);
                } else {
                    Collections.sort(arrayList);
                    files.add(saveToTmpFile(arrayList, tmpDir, files.size(), charset));
                    length = line.length();
                    arrayList = new ArrayList<>();
                    arrayList.add(line);
                }
            }
            Collections.sort(arrayList);
            files.add(saveToTmpFile(arrayList, tmpDir, files.size(), charset));
        } catch (IOException e) {
            throw  new UncheckedIOException(e);
        }

        return files;
    }

}
