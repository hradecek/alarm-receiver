package com.hradecek.alarm.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LinkedListMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public final Map<K, LinkedList<V>> values = Collections.synchronizedMap(new LinkedHashMap<>(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, true));

    public void add(K key, V value) {
        values.putIfAbsent(key, new LinkedList<>());
        LinkedList<V> list = values.get(key);
        list.add(value);
        values.put(key, list);
    }

    public List<V> push(K key, V value) {
        add(key, value);
        return get(key);
    }

    public V head(K key) {
        return values.get(key).getLast();
    }

    public List<V> get(K key) {
        return values.get(key);
    }

    public List<V> pop(K key) {
        return values.remove(key);
    }

    public boolean contains(K key) {
        return values.containsKey(key);
    }
}
