package com.swieghelm.redgnu.config.inject.annotation;

import com.swieghelm.redgnu.config.inject.ConfigDescription;

import java.lang.annotation.Annotation;

public class DefaultConfigDescription implements ConfigDescription {

    private final Config config;

    public DefaultConfigDescription(final Config config) {
        this.config = config;
    }

    @Override
    public String getKey() {
        return config.value();
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public Annotation getBindingAnnotation() {
        return config;
    }
}