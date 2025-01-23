package com.onyxdb.mdb.utils;

/**
 * @author foxleren
 */
public class CommonUtils {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
