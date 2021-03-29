package com.swieghelm.redgnu.config.inject.discovery;

import com.google.inject.TypeLiteral;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.Objects;

public class ParameterInjectionCandidate implements InjectionCandidate {

    private final Parameter parameter;
    private final TypeLiteral<?> type;

    ParameterInjectionCandidate(final Parameter parameter) {
        this.parameter = parameter;
        this.type = TypeLiteral.get(parameter.getParameterizedType());
    }

    @Override
    public TypeLiteral<?> getTargetType() {
        return type;
    }

    @Override
    public AnnotatedElement getElement() {
        return parameter;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this ||
                (obj instanceof ParameterInjectionCandidate && isEqual((ParameterInjectionCandidate) obj));
    }

    private boolean isEqual(final ParameterInjectionCandidate obj) {
        return Objects.equals(parameter, obj.parameter) &&
                Objects.equals(type, obj.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter, type);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " +
                parameter.getDeclaringExecutable().getDeclaringClass().getName() + "." +
                parameter.getDeclaringExecutable().getName() + "#" +
                parameter.getName();
    }
}
