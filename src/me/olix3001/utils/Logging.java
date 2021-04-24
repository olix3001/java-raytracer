package me.olix3001.utils;

public class Logging {
    public static <T> String arrayToString(T[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        for (T e : array) {
            builder.append(e.toString() + ", ");
        }
        builder.append(" ]");
        return builder.toString();
    }
}
