package com.swieghelm.redgnu.config.discovery;

import com.google.inject.TypeLiteral;

import java.lang.reflect.AnnotatedElement;

public interface InjectionCandidate {

    TypeLiteral<?> getTargetType();

    AnnotatedElement getElement();

}