package me.faln.chaoticenchants.registry;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Registry<K, V> {

    Map<K, V> getRegistry();

    default V get(final K key) {
        return this.getRegistry().get(key);
    }

    default Optional<V> getOpt(final K key) {
        if (this.getRegistry().containsKey(key)) {
            return Optional.ofNullable(this.getRegistry().get(key));
        }
        return Optional.empty();
    }

    default void put(final K key, final V value) {
        this.getRegistry().put(key, value);
    }

    default void putIfAbsent(final K key, final V value) {
        this.getRegistry().putIfAbsent(key, value);
    }

    default void remove(final K key) {
        this.getRegistry().remove(key);
    }

    default int size() {
        return this.getRegistry().size();
    }

    default boolean isEmpty() {
        return this.getRegistry().isEmpty();
    }

    default void putAll(final Map<? extends K, ? extends V> map) {
        this.getRegistry().putAll(map);
    }

    default void clear() {
        this.getRegistry().clear();
    }

    default boolean containsKey(final K key) {
        return this.getRegistry().containsKey(key);
    }

    default boolean containsValue(final V value) {
        return this.getRegistry().containsValue(value);
    }

    default void replace(final K key, final V newValue) {
        this.getRegistry().replace(key, newValue);
    }

    default Set<Map.Entry<K, V>> entrySet() {
        return this.getRegistry().entrySet();
    }

    default Set<K> keySet() {
        return this.getRegistry().keySet();
    }

    default Collection<V> values() {
        return this.getRegistry().values();
    }

}
