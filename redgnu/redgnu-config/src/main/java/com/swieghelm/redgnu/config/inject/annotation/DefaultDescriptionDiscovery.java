package com.swieghelm.redgnu.config.inject.annotation;

import com.swieghelm.redgnu.config.inject.discovery.DescriptionDiscovery;
import com.swieghelm.redgnu.config.inject.ConfigDescription;
import com.swieghelm.redgnu.config.inject.discovery.InjectionCandidate;

public class DefaultDescriptionDiscovery implements DescriptionDiscovery {

    @Override
    public ConfigDescription get(final InjectionCandidate injectionCandidate) {
        return null;
    }

}