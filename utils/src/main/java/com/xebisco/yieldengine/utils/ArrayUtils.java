package com.xebisco.yieldengine.utils;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayUtils {
    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static <T> T[] length(T[] first, int newLength) {
        return Arrays.copyOf(first, newLength);
    }

    public static <T> int find(T[] arr, T object) {
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == object) {
                return i;
            }
        }
        return -1;
    }

    public static <T> T[] insertLast(T[] arr, T object) {
        T[] result = Arrays.copyOf(arr, arr.length + 1);
        result[arr.length] = object;
        return result;
    }

    public static <T> T[] insertFirst(T[] arr, T object) {
        //noinspection unchecked
        T[] result = (T[]) Array.newInstance(arr.getClass().getComponentType(), arr.length + 1);
        result[0] = object;
        System.arraycopy(arr, 0, result, 1, arr.length);
        return result;
    }

    public static <T> T[] remove(T[] arr, int index) {
        T[] result = Arrays.copyOf(arr, arr.length - 1);
        System.arraycopy(arr, index + 1, result, index, arr.length - index - 1);
        return result;
    }

    public static <T> T[] remove(T[] arr, T object) {
        return remove(arr, find(arr, object));
    }

    public static <T> T[] shift(T[] arr, int from, int to) {
        if(from < 0 || from > arr.length || to < 0 || to > arr.length) {
            throw new IndexOutOfBoundsException();
        }
        T[] result = Arrays.copyOf(arr, arr.length);

        if(from > to) {
            System.arraycopy(arr, to, result, to + 1, from - to);
        } else if(from < to) {
            System.arraycopy(arr, from + 1, result, from, to - from);
        }

        result[to] = arr[from];
        return result;
    }
}
