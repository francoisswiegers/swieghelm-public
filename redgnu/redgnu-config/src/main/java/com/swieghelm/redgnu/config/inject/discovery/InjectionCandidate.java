package com.swieghelm.redgnu.config.inject.discovery;

import com.google.inject.TypeLiteral;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.Objects;

public interface InjectionCandidate {

    TypeLiteral<?> getTargetType();

    AnnotatedElement getElement();

}