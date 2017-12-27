package org.sav.externalsort.sorters;

import java.util.concurrent.RecursiveAction;

/**
 * Created by Andrey.Shilov on 28.11.2017.
 */
public class MergeSortTask extends RecursiveAction {

    private final String [] array1;
    private final int start;
    private final int end;
    private final int threshold;

    public MergeSortTask(String[] array1, int start, int end, int threshold) {
        this.array1 = array1;
        this.start = start;
        this.end = end;
        this.threshold = threshold;
    }

    @Override
    protected void compute() {
        if (start == end) {
            return;
        } else {
            int middle = (start + end)/2;
            int t = threshold/2;
            MergeSortTask task1 = new MergeSortTask(array1, start, middle, t);
            MergeSortTask task2 = new MergeSortTask(array1, middle + 1, end, t);
            if (t > 1) {
                invokeAll(task1, task2);
            } else {
                task1.compute();
                task2.compute();
            }

            merge(start, middle + 1, end);
        }
    }

    public void merge(int start, int middle, int end) {
        int lowerBound = start;
        int mid = middle-1;
        int length = end - start + 1;

        int indexWork = 0;
        String[] work;
        work = new String[length];

        while(lowerBound <= mid && middle <= end){
            if( array1[lowerBound].compareTo(array1[middle]) < 0 ) {
                work[indexWork++] = array1[lowerBound++];
            } else {
                work[indexWork++] = array1[middle++];
            }
        }

        while(lowerBound <= mid) {
            work[indexWork++] = array1[lowerBound++];
        }

        while(middle <= end) {
            work[indexWork++] = array1[middle++];
        }

        for(indexWork = 0; indexWork < length; indexWork++) {
            array1[start + indexWork] = work[indexWork];
        }
    }
}
