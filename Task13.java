import java.util.*;

interface CachePolicyStrategy {
    Object get(LinkedHashMap<String, Object> store, String key);
    void put(LinkedHashMap<String, Object> store, String key, Object value, int capacity);
}

class LruEvictionStrategy implements CachePolicyStrategy {
    public Object get(LinkedHashMap<String, Object> store, String key) {
        if (!store.containsKey(key)) return null;
        Object val = store.remove(key);
        store.put(key, val);
        return val;
    }

    public void put(LinkedHashMap<String, Object> store, String key, Object value, int capacity) {
        if (store.containsKey(key)) {
            store.remove(key);
        } else if (store.size() >= capacity) {
            store.remove(store.keySet().iterator().next());
        }
        store.put(key, value);
    }
}

class FifoEvictionStrategy implements CachePolicyStrategy {
    public Object get(LinkedHashMap<String, Object> store, String key) { return store.get(key); }

    public void put(LinkedHashMap<String, Object> store, String key, Object value, int capacity) {
        if (!store.containsKey(key) && store.size() >= capacity) {
            store.remove(store.keySet().iterator().next());
        }
        store.put(key, value);
    }
}

class TtlExpirationStrategy implements CachePolicyStrategy {
    private record Entry(Object val, long expiresAt) {}
    private final long ttlMillis;
    TtlExpirationStrategy(long ttlMillis) { this.ttlMillis = ttlMillis; }

    public Object get(LinkedHashMap<String, Object> store, String key) {
        Entry e = (Entry) store.get(key);
        if (e == null) return null;
        if (System.currentTimeMillis() > e.expiresAt()) {
            store.remove(key);
            return null;
        }
        return e.val();
    }

    public void put(LinkedHashMap<String, Object> store, String key, Object value, int capacity) {
        if (store.size() >= capacity) store.remove(store.keySet().iterator().next());
        store.put(key, new Entry(value, System.currentTimeMillis() + ttlMillis));
    }
}

class QuerySanitizer {
    String normalize(String sql, Map<String, Object> params) {
        if (sql.contains(";")) throw new IllegalArgumentException("Unsafe SQL");
        return sql.trim() + "::" + new TreeMap<>(params);
    }
}

class MemoryCacheStore {
    final int capacity;
    final LinkedHashMap<String, Object> map = new LinkedHashMap<>();
    MemoryCacheStore(int capacity) { this.capacity = capacity; }
}

class ConnectionPool {
    List<String> runSql(String sql, Map<String, Object> params) { return List.of("ROW_1:" + sql); }
}

class QueryMetricsRecorder {
    void record(boolean hit) { System.out.println("Cache hit=" + hit); }
}

class DataAccessFacade {
    private CachePolicyStrategy strategy;
    private final QuerySanitizer sanitizer = new QuerySanitizer();
    private final MemoryCacheStore cache;
    private final ConnectionPool pool = new ConnectionPool();
    private final QueryMetricsRecorder metrics = new QueryMetricsRecorder();

    DataAccessFacade(CachePolicyStrategy strategy, int capacity) {
        this.strategy = strategy;
        this.cache = new MemoryCacheStore(capacity);
    }

    void setStrategy(CachePolicyStrategy strategy) { this.strategy = strategy; }

    Object executeQuery(String sql, Map<String, Object> params) {
        String key = sanitizer.normalize(sql, params);
        Object hit = strategy.get(cache.map, key);
        if (hit != null) {
            metrics.record(true);
            return hit;
        }
        metrics.record(false);
        Object res = pool.runSql(sql, params);
        strategy.put(cache.map, key, res, cache.capacity);
        return res;
    }
}