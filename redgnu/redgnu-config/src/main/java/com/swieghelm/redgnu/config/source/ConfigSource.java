package com.swieghelm.redgnu.config.source;

import java.util.Optional;

public interface ConfigSource {

    Object getConfiguredValue(String key);

}