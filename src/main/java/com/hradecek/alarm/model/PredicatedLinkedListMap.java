package com.hradecek.alarm.model;

import java.util.List;
import java.util.function.Predicate;

public class PredicatedLinkedListMap<K, V> extends LinkedListMap<K, V> {

    private final Predicate<V> popPredicate;

    public PredicatedLinkedListMap(Predicate<V> popPredicate) {
        this.popPredicate = popPredicate;
    }

    @Override
    public List<V> push(K key, V value) {
        return popPredicate.test(value) ? popThenAdd(key, value) : super.push(key, value);
    }

    private List<V> popThenAdd(K key, V value) {
        final List<V> list = pop(key);
        list.add(value);

        return list;
    }

    public int size() {
        return super.values.keySet().size();
    }
}
