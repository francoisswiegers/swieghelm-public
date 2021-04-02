package com.swieghelm.redgnu.config.convert.common;

import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.convert.TypeConversion;
import com.swieghelm.redgnu.config.convert.exception.ConversionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.swieghelm.redgnu.config.type.CollectionUtil.createInstance;
import static com.swieghelm.redgnu.config.type.CollectionUtil.isCollection;
import static com.swieghelm.redgnu.config.type.CollectionUtil.isMap;
import static com.swieghelm.redgnu.config.type.TypeUtil.getParameterizedTypes;
import static java.util.Collections.singleton;

public class JavaStandardCollectionConversions implements TypeConversion {

    private static final Logger LOG = LoggerFactory.getLogger(JavaStandardCollectionConversions.class);

    private final TypeConversion scalarConversions;

    public JavaStandardCollectionConversions() {
        this(new JavaStandardScalarConversions());
    }

    public JavaStandardCollectionConversions(final TypeConversion scalarConversions) {
        this.scalarConversions = scalarConversions;
    }

    @Override
    public Object convert(final Object raw,
                          final TypeLiteral<?> targetType) {
        if (isCollection(targetType)) {
            return convertToCollection(raw, targetType);
        }
        else if (isMap(targetType)) {
            return convertToMap(raw, targetType);
        }
        else {
            LOG.debug("{} only converts to standard collection types", this);
            throw new ConversionNotFoundException(this, raw, targetType);
        }
    }

    private Object convertToCollection(final Object raw,
                                       final TypeLiteral<?> targetType) {
        final TypeLiteral<?> elementType = getParameterizedTypes(targetType).get(0);
        final Collection<Object> convertedValue = createInstance(targetType);
        final Collection<?> rawCol = raw instanceof Collection<?> ? (Collection<?>) raw : singleton(raw);
        for (final Object element : rawCol) {
            final Object convertedItem = scalarConversions.convert(element, elementType);
            convertedValue.add(convertedItem);
        }
        return convertedValue;
    }

    private Object convertToMap(final Object raw,
                                final TypeLiteral<?> targetType) {
        return null;
    }


}