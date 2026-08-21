/*
 * Decompiled with CFR 0.153-SNAPSHOT (11e700f-dirty).
 */
package com.trolmastercard.sexmod.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BiDirectionalMap<K, V> {
    final private HashMap<K, V> Map = new HashMap();
    final private HashMap<V, K> FlippedMap = new HashMap();

    public void put(K key, V value) {
        V oldValue = this.Map.put(key, value);
        this.FlippedMap.remove(oldValue);
        this.FlippedMap.put(value, key);
    }

    public V getbyKey(K key) {
        return this.Map.get(key);
    }

    public K getByValue(V value) {
        return this.FlippedMap.get(value);
    }

    public int size() {
        return this.Map.size();
    }

    public void removeByKey(K key) {
        V removedValue = this.Map.get(key);
        if (removedValue != null) {
            this.Map.remove(key);
            this.FlippedMap.remove(removedValue);
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return this.Map.entrySet();
    }

    public Set<K> keySet() {
        return this.Map.keySet();
    }

    public Set<V> valueSet() {
        return this.FlippedMap.keySet();
    }

    public void clear() {
        this.FlippedMap.clear();
        this.Map.clear();
    }
}

