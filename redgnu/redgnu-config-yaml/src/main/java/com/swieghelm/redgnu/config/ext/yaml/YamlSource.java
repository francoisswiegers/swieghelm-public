package com.swieghelm.redgnu.config.ext.yaml;

import com.swieghelm.redgnu.config.source.ConfigSource;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import java.io.InputStream;
import java.util.Map;

public class YamlSource implements ConfigSource {

    private static final String REGEX_DOT = "\\.";

    private final Map<String, Object> document;

    public YamlSource(final InputStream inputStream) {
        this(new Load(LoadSettings.builder().build()), inputStream);
    }

    public YamlSource(final Load load,
                      final InputStream inputStream) {
        this.document = (Map<String, Object>) load.loadFromInputStream(inputStream);
    }

    @Override
    public Object getConfiguredValue(final String key) {
        // Snakeyaml stores nested properties as Maps within Maps. We have to deconstruct that into a key-value
        final String[] keyComponents = key.split(REGEX_DOT);
        Object value = document.get(keyComponents[0]);
        for (int k = 1; k < keyComponents.length; k++) {
            if (!(value instanceof Map<?, ?>)) {
                return null;
            }
            value = ((Map<?, ?>)value).get(keyComponents[k]);
        }
        return value;
    }
}