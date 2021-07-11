package com.example.ebay.uitls;

import java.util.*;

public class FastCollections {

    public static class KeyValue<K, V> {
        private K key;
        private V value;

        public KeyValue(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    public static class KeyValueBuilder<K, V> {
        private List<KeyValue<K, V>> kvs = new ArrayList<>();

        public static <K, V> KeyValueBuilder<K, V> build(Class<K> kClass, Class<V> tClass) {
            return new KeyValueBuilder();
        }

        public KeyValueBuilder<K, V> keyValue(K key, V value) {
            this.kvs.add(new KeyValue<>(key, value));
            return this;
        }

        public List<KeyValue<K, V>> getKvs() {
            return kvs;
        }
    }

    public static <K, V> Map<K, V> map(KeyValueBuilder<K, V> keyValueBuilder) {
        Map<K, V> map = new HashMap<>();
        for (KeyValue<K, V> kv : keyValueBuilder.getKvs()) {
            map.put(kv.getKey(), kv.getValue());
        }
        return map;
    }

    /**
     * Map
     *
     * @param objs
     * @return
     */
    public static Map<String, Object> map(Object... objs) {
        if (objs.length > 0 && objs.length % 2 != 0) {
            throw new IllegalArgumentException("构建Map数据错误, 请以键值对的形式传入数据.");
        }

        Map<String, Object> map = new HashMap<>();
        mapPushEntry(map, objs);
        return map;
    }


    public static Map<String, String> mapString(String... objs) {
        if (objs.length > 0 && objs.length % 2 != 0) {
            throw new IllegalArgumentException("构建Map数据错误, 请以键值对的形式传入数据.");
        }
        Map<String, String> map = new HashMap<>();
        mapPushEntry(map, objs);
        return map;
    }

    private static <T> void mapPushEntry(Map<String, T> map, T[] objs) {
        for (int i = 1; i <= objs.length; i++) {
            if (i % 2 == 0) {
                continue;
            }
            String key = (String) objs[i - 1];
            map.put(key, objs[i]);
        }
    }


    /**
     * Set
     *
     * @param ts
     * @return
     */
    public static <T extends Object> Set<T> set(T... ts) {
        Set<T> result = new HashSet<T>();
        if (ts != null && ts.length > 0) {
            for (T t : ts) {
                if (t == null) {
                    continue;
                }
                result.add(t);
            }
        }
        return result;
    }

    /**
     * list
     *
     * @param ts
     * @return
     */
    public static <T extends Object> List<T> list(T... ts) {
        return Arrays.asList(ts);
    }
}
