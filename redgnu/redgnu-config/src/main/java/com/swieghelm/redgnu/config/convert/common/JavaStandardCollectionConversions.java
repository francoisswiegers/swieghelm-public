package com.swieghelm.redgnu.config.convert.common;

import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.convert.TypeConversion;
import com.swieghelm.redgnu.config.convert.exception.ConversionNotFoundException;
import com.swieghelm.redgnu.config.convert.exception.TypeConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.swieghelm.redgnu.config.type.CollectionUtil.createInstance;
import static com.swieghelm.redgnu.config.type.CollectionUtil.createMapInstance;
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
        final Collection<Object> targetCollection = createTargetCollection(raw, targetType);
        final Collection<?> rawCol = raw instanceof Collection<?> ? (Collection<?>) raw : singleton(raw);
        for (final Object element : rawCol) {
            final Object convertedItem = scalarConversions.convert(element, elementType);
            targetCollection.add(convertedItem);
        }
        return targetCollection;
    }

    private Collection<Object> createTargetCollection(Object raw, TypeLiteral<?> targetType) {
        try {
            return createInstance(targetType);
        }
        catch (final TypeConversionException e) {
            throw new ConversionNotFoundException(this, raw, targetType);
        }
    }

    private Object convertToMap(final Object raw,
                                final TypeLiteral<?> targetType) {

        if (!(raw instanceof Map<?, ?>)) {
            LOG.debug("Cannot convert {} to {}", raw, targetType);
            throw new ConversionNotFoundException(this, raw, targetType);
        }

        final Map<Object, Object> rawMap = (Map<Object, Object>) raw;
        final List<TypeLiteral<?>> parameterizedTypes = getParameterizedTypes(targetType);
        final TypeLiteral<?> keyType = parameterizedTypes.get(0);
        final TypeLiteral<?> valueType = parameterizedTypes.get(1);

        final Map<Object, Object> targetMap = createTargetMap(raw, targetType);

        for (final Map.Entry<Object, Object> element : rawMap.entrySet()) {
            final Object convertedEntryKey = scalarConversions.convert(element.getKey(), keyType);
            final Object convertedEntryValue = element.getValue() != null ?
                    scalarConversions.convert(element.getValue(), valueType) :
                    null;
            targetMap.put(convertedEntryKey, convertedEntryValue);
        }
        return targetMap;
    }

    private Map<Object, Object> createTargetMap(final Object raw,
                                                final TypeLiteral<?> targetType) {
        try {
            return createMapInstance(targetType);
        }
        catch (final TypeConversionException e) {
            throw new ConversionNotFoundException(this, raw, targetType);
        }
    }

}