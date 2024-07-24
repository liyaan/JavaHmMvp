package com.liyaan.study.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class SparseRawArray<K extends Number, E> {
    private final LinkedHashMap<K, E> mMap;
    private Iterator<K> mIterator;
    private int mIndex;
    private boolean mIndexDirty;
    private K mCurrentKey;

    public SparseRawArray() {
        mMap = new LinkedHashMap<>();
        resetIndex();
    }

    public E get(K key) {
        return get(key, null);
    }

    public E get(K key, E valueIfKeyNotFound) {
        E value = mMap.get(key);
        return value != null ? value : valueIfKeyNotFound;
    }

    public void delete(K key) {
        mMap.remove(key);
        mIndexDirty = true;
    }

    public void remove(K key) {
        delete(key);
    }

    public void put(K key, E value) {
        mMap.put(key, value);
        mIndexDirty = true;
    }

    public int size() {
        return mMap.size();
    }

    public K keyAt(int index, K keyIfIndexNotExist) {
        if (mIndex == index && !mIndexDirty) {
            return mCurrentKey;
        }

        if (mIndexDirty || mIndex > index) {
            resetIndex();
        }

        while (mIterator.hasNext()) {
            if (++mIndex == index) {
                mCurrentKey = mIterator.next();
                return mCurrentKey;
            }
        }

        mIndexDirty = true;

        return keyIfIndexNotExist;
    }

    public void clear() {
        mMap.clear();
        mIndexDirty = true;
    }

    private void resetIndex() {
        mIndex = -1;
        mIterator = mMap.keySet().iterator();
        mCurrentKey = null;
        mIndexDirty = false;
    }
}
