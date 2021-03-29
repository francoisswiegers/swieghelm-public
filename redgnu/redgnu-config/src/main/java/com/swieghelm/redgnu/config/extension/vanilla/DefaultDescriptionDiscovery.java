package com.swieghelm.redgnu.config.extension.vanilla;

import com.swieghelm.redgnu.config.inject.ConfigDescription;
import com.swieghelm.redgnu.config.discovery.DescriptionDiscovery;
import com.swieghelm.redgnu.config.discovery.InjectionCandidate;

public class DefaultDescriptionDiscovery implements DescriptionDiscovery {

    @Override
    public ConfigDescription get(final InjectionCandidate injectionCandidate) {
        final Config configAnnotation = injectionCandidate.getElement().getAnnotation(Config.class);
        return configAnnotation != null ?
                new DefaultConfigDescription(configAnnotation) :
                null;
    }

}