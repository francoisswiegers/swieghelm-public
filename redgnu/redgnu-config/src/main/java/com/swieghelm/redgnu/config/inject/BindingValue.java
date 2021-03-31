package com.swieghelm.redgnu.config.inject;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

import static com.swieghelm.redgnu.config.type.OptionalUtil.isOptional;

public class BindingValue<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BindingValue.class);

    private final BindingKey bindingKey;
    private final T value;

    public BindingValue(final BindingKey bindingKey,
                        final T value) {
        this.bindingKey = bindingKey;
        this.value = value;
    }

    public void bind(final Binder binder) {
        final TypeLiteral<?> targetType = bindingKey.getTargetType();
        final Annotation annotation = bindingKey.getAnnotation();
        if (value == null) {
            if (isOptional(targetType)) {
                binder.bind((TypeLiteral<Optional<T>>) targetType)
                      .annotatedWith(annotation)
                      .toInstance(Optional.empty());
            }
            else {
                LOG.error("Cannot inject null value into non-Optional {}", bindingKey);
                // TODO Add to error log
            }
        }
        else {
            if (isOptional(targetType)) {
                binder.bind((TypeLiteral<Optional<T>>) targetType)
                      .annotatedWith(annotation)
                      .toInstance(Optional.of(value));
            }
            else {
                binder.bind((TypeLiteral<T>) targetType)
                      .annotatedWith(annotation)
                      .toInstance(value);
            }
        }
    }

    public void validate(final Object otherValue) {
        if (!Objects.equals(value, otherValue)) {
            LOG.error("{} already bound to {}", bindingKey, value);
            // TODO Add to error log
        }
        else {
            LOG.debug("Duplicate binding detected. We'll ignore the duplicate");
        }
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof BindingValue && isEqual((BindingValue<?>) obj));
    }

    private boolean isEqual(BindingValue<?> obj) {
        return Objects.equals(bindingKey, obj.bindingKey) &&
                Objects.equals(value, obj.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindingKey, value);
    }

    @Override
    public String toString() {
        return bindingKey + "->" + value;
    }

}