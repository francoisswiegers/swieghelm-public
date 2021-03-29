package com.swieghelm.redgnu.config.inject;

import com.swieghelm.redgnu.config.convert.TypeConversion;
import com.swieghelm.redgnu.config.discovery.DescriptionDiscovery;
import com.swieghelm.redgnu.config.source.ConfigSource;
import org.reflections.Reflections;

import static com.google.common.base.Verify.verifyNotNull;

public class ConfigModuleConfig {

    private final ConfigSource configSource;
    private final DescriptionDiscovery discovery;
    private final TypeConversion typeConversion;
    private final Reflections reflections;

    public ConfigModuleConfig(final ConfigSource configSource,
                              final DescriptionDiscovery discovery,
                              final TypeConversion typeConversion,
                              final Reflections reflections) {
        this.configSource = verifyNotNull(configSource, "ConfigSource must be nonnull");
        this.discovery = verifyNotNull(discovery, "Discovery must be nonnull");
        this.typeConversion = verifyNotNull(typeConversion, "TypeConversion must be nonnull");
        this.reflections = verifyNotNull(reflections, "Reflections must be nonnull");
    }

    public ConfigSource getConfigSource() {
        return configSource;
    }

    public DescriptionDiscovery getDiscovery() {
        return discovery;
    }

    public TypeConversion getTypeConversion() {
        return typeConversion;
    }

    public Reflections getReflections() {
        return reflections;
    }

    public static class ConfigModuleConfigBuilder {

        private ConfigSource configSource;
        private DescriptionDiscovery descriptionDiscovery;
        private TypeConversion typeConversion;
        private Reflections reflections;

        public ConfigModuleConfigBuilder setConfigSource(final ConfigSource configSource) {
            this.configSource = configSource;
            return this;
        }

        public ConfigModuleConfigBuilder setDescriptionDiscovery(final DescriptionDiscovery descriptionDiscovery) {
            this.descriptionDiscovery = descriptionDiscovery;
            return this;
        }

        public ConfigModuleConfigBuilder setTypeConversion(final TypeConversion typeConversion) {
            this.typeConversion = typeConversion;
            return this;
        }

        public ConfigModuleConfigBuilder setReflections(final Reflections reflections) {
            this.reflections = reflections;
            return this;
        }

        public ConfigModule build() {
            return new ConfigModule(
                    new ConfigModuleConfig(configSource, descriptionDiscovery, typeConversion, reflections));
        }

    }
}