package com.hy.basicproject.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author qq8585083
 *
 */
public final class CloseUtil {
    private CloseUtil() { }


    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void close(Closeable... params) {
        if (null != params) {
            try {
                for (Closeable closeable : params) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
