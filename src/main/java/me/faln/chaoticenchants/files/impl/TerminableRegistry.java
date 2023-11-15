package me.faln.chaoticenchants.files.impl;

import lombok.NoArgsConstructor;
import me.faln.chaoticenchants.files.AbstractRegistry;
import me.lucko.helper.terminable.Terminable;

import java.util.Map;

@NoArgsConstructor
public abstract class TerminableRegistry<K, V> extends AbstractRegistry<K, V> implements Terminable {

    protected TerminableRegistry(final Map<K, V> backingMap) {
        super(backingMap);
    }
}
