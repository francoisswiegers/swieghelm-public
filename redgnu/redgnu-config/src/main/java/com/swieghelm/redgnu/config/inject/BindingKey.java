package com.swieghelm.redgnu.config.inject;

import com.google.inject.TypeLiteral;

import java.lang.annotation.Annotation;
import java.util.Objects;

public class BindingKey {

    private final TypeLiteral<?> targetType;
    private final Annotation annotation;

    public BindingKey(final TypeLiteral<?> targetType,
                      final Annotation annotation) {
        this.targetType = targetType;
        this.annotation = annotation;
    }

    public TypeLiteral<?> getTargetType() {
        return targetType;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof BindingKey && isEqual((BindingKey) obj));
    }

    private boolean isEqual(final BindingKey obj) {
        return Objects.equals(targetType, obj.targetType) &&
                Objects.equals(annotation, obj.annotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType, annotation);
    }

    @Override
    public String toString() {
        return targetType + "[annotation=" + annotation + ']';
    }
}
