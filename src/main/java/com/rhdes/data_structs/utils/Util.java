package com.rhdes.data_structs.utils;

public class Util {

    public static enum Resize {
        LARGER, SMALLER
    }

    public static <T> T[] resizeIfNeeded(Resize resizeCheck, T[] data,
                                            int numItems, int containerSize,
                                            int minContainerSize, int maxContainerSize) {
        if (resizeCheck == Resize.SMALLER
                && numItems < containerSize / 2
                && containerSize > minContainerSize) {
            return resizeSmaller(data);
        } else if (resizeCheck == Resize.LARGER
                        && numItems == containerSize
                        && containerSize < maxContainerSize) {
            return resizeLarger(data);
        } else {
            return data;
        }
    }

    public static <T> T[] resizeSmaller(T[] data) {
        return null;
    }

    public static <T> T[] resizeLarger(T[] data) {
        return null;
    }
}
