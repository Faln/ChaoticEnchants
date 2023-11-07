package me.faln.chaoticenchants.registry;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRegistry<K, V> implements Registry<K, V> {

    private final Map<K, V> map;

    protected AbstractRegistry() {
        this(new HashMap<>());
    }

    protected AbstractRegistry(final Map<K, V> map) {
        this.map = map;
    }

    @Override
    public Map<K, V> getRegistry() {
        return this.map;
    }
}
