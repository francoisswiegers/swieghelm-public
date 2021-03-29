package com.swieghelm.redgnu.config.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.convert.exception.ConversionNotFoundException;
import com.swieghelm.redgnu.config.discovery.ParameterInjectionCandidate;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;

public class ConfigModule extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigModule.class);

    private final ConfigModuleConfig config;

    public ConfigModule(final ConfigModuleConfig config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        final Map<BindingKey, Object> boundValues = new HashMap<>();
        final Reflections reflections = new Reflections(config.getReflections().getConfiguration(),
                new MethodAnnotationsScanner());
        final Set<Constructor> injectingConstructors = new HashSet<>();
        injectingConstructors.addAll(reflections.getConstructorsAnnotatedWith(Inject.class));
        injectingConstructors.addAll(reflections.getConstructorsAnnotatedWith(javax.inject.Inject.class));

        for (final Constructor<?> constructor : injectingConstructors) {
            for (final Parameter parameter : constructor.getParameters()) {
                final ParameterInjectionCandidate injectionCandidate = new ParameterInjectionCandidate(parameter);
                final ConfigDescription configDescription = config.getDiscovery().get(injectionCandidate);
                if (configDescription != null) {
                    final String key = configDescription.getKey();
                    final Object defaultValue = configDescription.getDefaultValue();
                    final Object configuredValue = config.getConfigSource().getConfiguredValue(key);
                    final Object valueToBind = configuredValue != null ? configuredValue : defaultValue;
                    final TypeLiteral<?> targetType = injectionCandidate.getTargetType();
                    final Annotation annotation = configDescription.getBindingAnnotation();
                    try {
                        final BindingKey bindingKey = new BindingKey(targetType, annotation);
                        final Object convertedValue = valueToBind != null ?
                                config.getTypeConversion().convert(valueToBind, targetType) :
                                null;
                        final Object boundValue = boundValues.get(bindingKey);
                        if (boundValue != null) {
                            if (!boundValue.equals(convertedValue)) {
                                LOG.error("{} already has previously bound value of {}", bindingKey, boundValue);
                                // TODO Add to error log
                            }
                            else {
                                LOG.debug("Duplicate binding detected. We'll ignore the duplicate");
                            }
                        }
                        else {
                            if (convertedValue == null) {
                                if (Optional.class.equals(targetType.getRawType())) {
                                    bind((TypeLiteral<Optional>) targetType)
                                            .annotatedWith(annotation)
                                            .toInstance(Optional.empty());
                                }
                                else {
                                    LOG.error("Cannot inject null value into non-Optional {}");
                                    // TODO Add to error log
                                }
                            }
                            else {
                                if (Optional.class.equals(targetType.getRawType())) {
                                    bind((TypeLiteral<Optional>) targetType)
                                            .annotatedWith(annotation)
                                            .toInstance(Optional.of(convertedValue));
                                }
                                else {
                                    LOG.error("Cannot inject null value into non-Optional {}");
                                    bind((TypeLiteral<Object>) targetType)
                                            .annotatedWith(annotation)
                                            .toInstance(convertedValue);
                                }
                                boundValues.put(bindingKey, convertedValue);
                            }
                        }
                    }
                    catch (final ConversionNotFoundException e) {
                        LOG.error("Could not find a converter for {} to type {}", e.getRawValue(), e.getTargetType());
                        // TODO Add to error log
                    }
                }
            }
        }
    }

    public static ConfigModuleConfig.ConfigModuleConfigBuilder newBuilder() {
        return new ConfigModuleConfig.ConfigModuleConfigBuilder();
    }
}