package watchdogagent.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ran Zhang
 * @since 2024/3/27
 */
public class LocalStorage<K, V> {
    private ConcurrentHashMap<K, V> store;

    public static <K, V> LocalStorage<K, V> newStorage() {
        LocalStorage<K, V> storage = new LocalStorage<>();
        storage.store = new ConcurrentHashMap<>();
        return storage;
    }

    public void put(K k, V v) {
        this.store.put(k, v);
    }

    public Map<K, V> getStore() {
        return store;
    }

    public V get(K k) {
        return this.store.get(k);
    }

    public Set<Map.Entry<K, V>> getAll() {
        return this.store.entrySet();
    }

    public void clear() {
        this.store.clear();
    }

    public boolean isEmpty() {
        return this.store.isEmpty();
    }
}
