package com.swieghelm.redgnu.config.source;

import java.util.Optional;

public interface ConfigSource {

    Optional<String> getConfiguredValue(String key);

}