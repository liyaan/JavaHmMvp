package com.liyaan.study.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class SparseArray<E> extends SparseRawArray<Integer, E> {
    public int keyAt(int index) {
        return keyAt(index, -1);
    }
}
