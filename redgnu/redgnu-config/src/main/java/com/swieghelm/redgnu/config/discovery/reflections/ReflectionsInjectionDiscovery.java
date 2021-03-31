package com.swieghelm.redgnu.config.discovery.reflections;

import com.google.inject.Inject;
import com.swieghelm.redgnu.config.discovery.*;
import com.swieghelm.redgnu.config.inject.ConfigDescription;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Consumer;

import static com.google.common.base.Verify.verifyNotNull;
import static java.util.Collections.unmodifiableSet;

/**
 * Discovery of injection points using the Reflections library
 */
public class ReflectionsInjectionDiscovery implements InjectionDiscovery {

    private final Set<ConfigInjection> injections;

    public ReflectionsInjectionDiscovery(final Package packageName,
                                         final DescriptionDiscovery descriptionDiscovery) {
        this(packageName.getName(), descriptionDiscovery);
    }

    public ReflectionsInjectionDiscovery(final String packageName,
                                         final DescriptionDiscovery descriptionDiscovery) {
        this(new Reflections(packageName, new MethodAnnotationsScanner()), descriptionDiscovery);
    }

    public ReflectionsInjectionDiscovery(final Reflections reflections,
                                         final DescriptionDiscovery descriptionDiscovery) {
        verifyNotNull(reflections, "Reflections must be nonnull");
        final Set<Constructor> injectingConstructors = new HashSet<>();
        injectingConstructors.addAll(reflections.getConstructorsAnnotatedWith(Inject.class));
        injectingConstructors.addAll(reflections.getConstructorsAnnotatedWith(javax.inject.Inject.class));
        final Set<ConfigInjection> collectedInjections = new HashSet<>();
        for (final Constructor<?> constructor : injectingConstructors) {
            for (final Parameter parameter : constructor.getParameters()) {
                final ParameterInjectionCandidate injectionCandidate = new ParameterInjectionCandidate(parameter);
                final ConfigDescription configDescription = descriptionDiscovery.get(injectionCandidate);
                if (configDescription != null) {
                    collectedInjections.add(new ConfigInjection(injectionCandidate, configDescription));
                }
            }
        }
        this.injections = unmodifiableSet(collectedInjections);
    }

    @Override
    public Iterator<ConfigInjection> iterator() {
        return injections.iterator();
    }

    @Override
    public void forEach(final Consumer<? super ConfigInjection> action) {
        injections.forEach(action);
    }

    @Override
    public Spliterator<ConfigInjection> spliterator() {
        return injections.spliterator();
    }

}