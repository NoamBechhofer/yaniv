package com.noambechhofer.yaniv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SetSorter {
    /**
     * Convert a {@link Set} into a sorted {@link List}
     * 
     * @param <T> the class of the objects in the Set
     * @param set the set to be converted
     * @return the sorted list
     */
    public static <T extends Comparable<? super T>> List<T> sort(Set<T> set) {
        List<T> tmp = new ArrayList<T>(set);

        Collections.sort(tmp);

        return tmp;
    }
}
