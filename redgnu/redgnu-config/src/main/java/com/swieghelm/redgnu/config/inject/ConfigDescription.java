package com.swieghelm.redgnu.config.inject;

import java.lang.annotation.Annotation;

public interface ConfigDescription {

    String getKey();

    Object getDefaultValue();

    Annotation getBindingAnnotation();

}