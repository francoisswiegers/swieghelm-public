package com.swieghelm.redgnu.config.ext.yaml;

import com.swieghelm.redgnu.config.source.ConfigSource;
import org.snakeyaml.engine.v2.api.Load;

import java.io.InputStream;
import java.util.Map;

public class YamlSource implements ConfigSource {

    private final Map<String, Object> document;

    public YamlSource(final Load load,
                      final InputStream inputStream) {
        this.document = (Map<String, Object>) load.loadFromInputStream(inputStream);
    }

    @Override
    public Object getConfiguredValue(final String key) {
        return document.get(key);
    }
}