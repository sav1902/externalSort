package org.sav.externalsort.utils;

/**
 * Created by Andrey.Shilov
 */
public final class DurstenfeldMix {

    private DurstenfeldMix() {
        super();
    }

    public static void mix(String[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            int randomIndex = (int) (Math.random()*i);
            String source = array[i];
            array[i] = array[randomIndex];
            array[randomIndex] = source;
        }
    }
}
