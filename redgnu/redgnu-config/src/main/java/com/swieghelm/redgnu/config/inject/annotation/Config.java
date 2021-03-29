package com.swieghelm.redgnu.config.inject.annotation;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
@BindingAnnotation
public @interface Config {

    /**
     * The key of the configuration item that should be injected.
     */
    String value() default "#EMPTY";

}