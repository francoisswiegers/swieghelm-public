package com.swieghelm.redgnu.config.inject;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.convert.TypeConversion;
import com.swieghelm.redgnu.config.convert.exception.ConversionNotFoundException;
import com.swieghelm.redgnu.config.discovery.ConfigInjection;
import com.swieghelm.redgnu.config.discovery.InjectionCandidate;
import com.swieghelm.redgnu.config.discovery.InjectionDiscovery;
import com.swieghelm.redgnu.config.source.ConfigSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Verify.verifyNotNull;
import static com.swieghelm.redgnu.config.type.OptionalUtil.getConversionType;
import static com.swieghelm.redgnu.config.type.OptionalUtil.isOptional;

public class ConfigModule extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigModule.class);

    private final ConfigSource configSource;
    private final InjectionDiscovery injectionDiscovery;
    private final TypeConversion typeConversion;

    public ConfigModule(final ConfigModuleConfig config) {
        this(config.getConfigSource(), config.getDiscovery(), config.getTypeConversion());
    }

    public ConfigModule(final ConfigSource configSource,
                        final InjectionDiscovery injectionDiscovery,
                        final TypeConversion typeConversion) {
        this.configSource = verifyNotNull(configSource, "ConfigSource must be nonnull");
        this.injectionDiscovery = verifyNotNull(injectionDiscovery, "Discovery must be nonnull");
        this.typeConversion = verifyNotNull(typeConversion, "TypeConversion must be nonnull");
    }

    @Override
    protected void configure() {
        final Map<BindingKey, BindingValue<?>> boundValues = new HashMap<>();
        for (final ConfigInjection injection : injectionDiscovery) {

            final ConfigDescription description = injection.getDescription();
            final InjectionCandidate injectionCandidate = injection.getInjectionCandidate();

            final String key = description.getKey();
            final Object defaultValue = description.getDefaultValue();
            final Object configuredValue = configSource.getConfiguredValue(key);
            final Object valueToBind = configuredValue != null ? configuredValue : defaultValue;
            final TypeLiteral<?> targetType = injectionCandidate.getTargetType();
            final TypeLiteral<?> conversionTargetType = getConversionType(targetType);
            final Annotation annotation = description.getBindingAnnotation();

            try {
                final BindingKey bindingKey = new BindingKey(targetType, annotation);
                final Object convertedValue = valueToBind != null ?
                        typeConversion.convert(valueToBind, conversionTargetType) :
                        null;
                final BindingValue<?> boundValue = boundValues.get(bindingKey);
                if (boundValue == null) {
                    final BindingValue<?> bindingValue = new BindingValue<>(bindingKey, convertedValue);
                    bindingValue.bind(binder());
                    boundValues.put(bindingKey, bindingValue);
                }
                else {
                    boundValue.validate(convertedValue);
                }
            }
            catch (final ConversionNotFoundException e) {
                LOG.error("Could not find a converter for {} to type {}", e.getRawValue(), e.getTargetType());
                // TODO Add to error log
            }
        }
    }

    public static ConfigModuleConfig.ConfigModuleConfigBuilder newBuilder() {
        return new ConfigModuleConfig.ConfigModuleConfigBuilder();
    }
}