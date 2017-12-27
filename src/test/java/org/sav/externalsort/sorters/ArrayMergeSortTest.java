package org.sav.externalsort.sorters;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.sav.externalsort.utils.DurstenfeldMix;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by Andrey.Shilov on 28.11.2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrayMergeSortTest {
    private DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static String[] sourceArray;
    private String[] testArray;

    @BeforeClass
    public static void beforeClass() {
        int size = 10_000_000;
        sourceArray = new String[size];
        for (int i = 0; i < sourceArray.length; i++) {
            sourceArray[i] = String.valueOf(i);
        }
        DurstenfeldMix.mix(sourceArray);
    }

    @Before
    public void before() {
        Runtime.getRuntime().gc();
        testArray = sourceArray.clone();
    }

    @Test
    public void fourThreadSortTest() {
        Date start = new Date();
        System.out.println(dateFormat.format(start));
        new MergeSortTask(testArray, 0, testArray.length-1, 4).compute();
        Date end = new Date();
        System.out.println(dateFormat.format(end));
        System.out.println(end.getTime() - start.getTime());

        String  prev = testArray[0];
        for (int i = 1; i < testArray.length; i++) {
            assertTrue("", prev.compareTo(testArray[i]) < 0);
            prev = testArray[i];
        }
    }

    @Test
    public void oneThreadSortTest() {
        Date start = new Date();
        System.out.println(dateFormat.format(start));
        new MergeSortTask(testArray, 0, testArray.length-1, 1).compute();
        Date end = new Date();
        System.out.println(dateFormat.format(end));
        System.out.println(end.getTime() - start.getTime());

        String  prev = testArray[0];
        for (int i = 1; i < testArray.length; i++) {
            assertTrue("", prev.compareTo(testArray[i]) < 0);
            prev = testArray[i];
        }
    }
}
