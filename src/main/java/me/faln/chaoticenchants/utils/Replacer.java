package me.faln.chaoticenchants.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class Replacer {

    private final Map<String, String> replacers = new WeakHashMap<>();

    public Replacer add(final String placeholder, final int replacer) {
        return this.add(placeholder, String.valueOf(replacer));
    }

    public Replacer add(final String placeholder, final double replacer) {
        return this.add(placeholder, String.valueOf(replacer));
    }

    public Replacer add(final String placeholder, final float replacer) {
        return this.add(placeholder, String.valueOf(replacer));
    }

    public Replacer add(final String placeholder, final long replacer) {
        return this.add(placeholder, String.valueOf(replacer));
    }

    public Replacer add(final String placeholder, final String replacer) {
        this.replacers.putIfAbsent(placeholder, replacer);
        return this;
    }

    public String parse(String message) {

        if (message == null) {
            return null;
        }

        for (Map.Entry<String, String> entry : this.replacers.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }

    public List<String> parse(final List<String> message) {

        if (message == null) {
            return new LinkedList<>();
        }

        return message.stream()
                .map(this::parse)
                .toList();
    }
}
