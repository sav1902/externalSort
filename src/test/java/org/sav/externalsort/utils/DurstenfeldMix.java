package org.sav.externalsort.utils;

/**
 * Created by Andrey.Shilov on 30.10.2017.
 */
public class DurstenfeldMix {

    public static void mix(String[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            int randomIndex = (int) (Math.random()*i);
            String source = array[i];
            array[i] = array[randomIndex];
            array[randomIndex] = source;
        }
    }
}
