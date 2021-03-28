package com.swieghelm.redgnu.config.source.impl;

import com.swieghelm.redgnu.config.source.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MapConfigSource implements ConfigSource {

    private final Map<String, String> configItems;

    public MapConfigSource(final Map<String, String> configItems) {
        this.configItems = new HashMap<>(configItems);
    }

    @Override
    public Optional<String> getConfiguredValue(final String key) {
        return Optional.ofNullable(configItems.get(key));
    }
}