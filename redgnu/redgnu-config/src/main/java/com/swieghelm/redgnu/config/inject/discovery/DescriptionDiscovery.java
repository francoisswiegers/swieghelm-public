package com.swieghelm.redgnu.config.inject.discovery;

import com.swieghelm.redgnu.config.inject.ConfigDescription;

public interface DescriptionDiscovery {

    ConfigDescription get(InjectionCandidate injectionCandidate);

}