package com.gtp.tradeapp.util;

import java.util.Collection;
import java.util.Iterator;

public class IteratorUtil {

    public static int getSize(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection<?>) iterable).size();
        } else {
            Integer count = 0;
            Iterator<?> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
            return count;
        }
    }
}
