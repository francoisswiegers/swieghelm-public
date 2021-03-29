package com.swieghelm.redgnu.config.extension.vanilla;

import com.swieghelm.redgnu.config.inject.ConfigDescription;

import java.lang.annotation.Annotation;
import java.util.Objects;

import static com.swieghelm.redgnu.config.extension.vanilla.DefaultConstants.valueOrUndefined;

public class DefaultConfigDescription implements ConfigDescription {

    private final Config config;

    public DefaultConfigDescription(final Config config) {
        this.config = config;
    }

    @Override
    public String getKey() {
        return valueOrUndefined(config.value());
    }

    @Override
    public Object getDefaultValue() {
        return valueOrUndefined(config.defaultValue());
    }

    @Override
    public Annotation getBindingAnnotation() {
        return config;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof DefaultConfigDescription && isEqual((DefaultConfigDescription)obj));
    }

    private boolean isEqual(final DefaultConfigDescription obj) {
        return Objects.equals(config, obj.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(config);
    }

    @Override
    public String toString() {
       return Objects.toString(config);
    }
}