package com.swieghelm.redgnu.config.extension.vanilla;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.swieghelm.redgnu.config.extension.vanilla.DefaultConstants.UNDEFINED;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotation used to denote injectable configuration properties.
 */
@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
@BindingAnnotation
public @interface Config {

    /**
     * The key of the configuration item that should be injected.
     */
    String value() default UNDEFINED;

    String defaultValue() default UNDEFINED;

}