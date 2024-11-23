package com.imstargg.batch.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractInMemoryCacheRepository<K, V> {

    private static final Logger log = LoggerFactory.getLogger(AbstractInMemoryCacheRepository.class);

    @FunctionalInterface
    public interface Initializer<V> {

        List<V> findAll();
    }

    private final ConcurrentHashMap<K, V> cache;

    protected AbstractInMemoryCacheRepository() {
        this.cache = new ConcurrentHashMap<>();
    }

    protected AbstractInMemoryCacheRepository(Initializer<V> initializer) {
        this.cache = new ConcurrentHashMap<>(initializer.findAll().stream()
                .collect(Collectors.toMap(this::key, value -> value))
        );
    }

    public Optional<V> find(K key) {
        if (cache.containsKey(key)) {
            return Optional.ofNullable(this.cache.get(key));
        }

        log.debug("[{}] Cache miss key: {}", getClass(), key);
        Optional<V> data = findData(key);
        data.ifPresent(value -> cache.put(key, value));
        return data;
    }

    public V save(V value) {
        K key = key(value);
        V savedValue = saveData(value);
        cache.put(key, savedValue);
        return savedValue;
    }

    protected abstract K key(V value);

    protected abstract Optional<V> findData(K key);

    protected abstract V saveData(V value);
}
