package com.swieghelm.redgnu.config.inject;

import com.swieghelm.redgnu.config.convert.TypeConversion;
import com.swieghelm.redgnu.config.discovery.InjectionDiscovery;
import com.swieghelm.redgnu.config.source.ConfigSource;

import static com.google.common.base.Verify.verifyNotNull;

public class ConfigModuleConfig {

    private final ConfigSource configSource;
    private final InjectionDiscovery discovery;
    private final TypeConversion typeConversion;

    public ConfigModuleConfig(final ConfigSource configSource,
                              final InjectionDiscovery discovery,
                              final TypeConversion typeConversion) {
        this.configSource = verifyNotNull(configSource, "ConfigSource must be nonnull");
        this.discovery = verifyNotNull(discovery, "Discovery must be nonnull");
        this.typeConversion = verifyNotNull(typeConversion, "TypeConversion must be nonnull");
    }

    public ConfigSource getConfigSource() {
        return configSource;
    }

    public InjectionDiscovery getDiscovery() {
        return discovery;
    }

    public TypeConversion getTypeConversion() {
        return typeConversion;
    }

    public static class ConfigModuleConfigBuilder {

        private ConfigSource configSource;
        private InjectionDiscovery discovery;
        private TypeConversion typeConversion;

        public ConfigModuleConfigBuilder setConfigSource(final ConfigSource configSource) {
            this.configSource = configSource;
            return this;
        }

        public ConfigModuleConfigBuilder setDiscovery(final InjectionDiscovery discovery) {
            this.discovery = discovery;
            return this;
        }

        public ConfigModuleConfigBuilder setTypeConversion(final TypeConversion typeConversion) {
            this.typeConversion = typeConversion;
            return this;
        }

        public ConfigModule build() {
            return new ConfigModule(
                    new ConfigModuleConfig(configSource, discovery, typeConversion));
        }

    }
}