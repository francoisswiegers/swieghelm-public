package com.swieghelm.redgnu.config.convert.common;

import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.convert.TypeConversion;
import com.swieghelm.redgnu.config.convert.exception.ConversionNotFoundException;
import com.swieghelm.redgnu.config.convert.exception.TypeConversionFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A set of very simple, straightforward type conversions to Java common types.
 */
public class SimpleJavaStandardTypeConversions implements TypeConversion {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleJavaStandardTypeConversions.class);

    private static final Map<TypeLiteral<?>, Function<Object, Object>> CONVERTERS = new HashMap<>();

    static {
        CONVERTERS.put(TypeLiteral.get(String.class), Object::toString);
        CONVERTERS.put(TypeLiteral.get(Byte.class), raw -> Byte.valueOf(raw.toString()));
        CONVERTERS.put(TypeLiteral.get(Short.class), raw -> Short.valueOf(raw.toString()));
        CONVERTERS.put(TypeLiteral.get(Boolean.class), raw -> Boolean.valueOf(raw.toString()));
        CONVERTERS.put(TypeLiteral.get(Integer.class), raw -> Integer.valueOf(raw.toString()));
        CONVERTERS.put(TypeLiteral.get(Long.class), raw -> Long.valueOf(raw.toString()));
        CONVERTERS.put(TypeLiteral.get(Float.class), raw -> Float.valueOf(raw.toString()));
        CONVERTERS.put(TypeLiteral.get(Double.class), raw -> Double.valueOf(raw.toString()));
        CONVERTERS.put(TypeLiteral.get(BigDecimal.class), raw -> new BigDecimal(raw.toString()));
    }

    @Override
    public Object convert(final Object raw,
                          final TypeLiteral<?> targetType) {

        final Function<Object, Object> converter = CONVERTERS.get(targetType);
        if (converter != null) {
            LOG.debug("Trying: {}", converter);
            try {
                return converter.apply(raw);
            }
            catch (final Exception e) {
                LOG.debug("Exception caught converting {} to {}", raw, targetType);
                throw new TypeConversionFailure(raw, targetType, e);
            }
        }

        LOG.debug("Did not find any converters for {} to {} in {}", raw, targetType, this);
        throw new ConversionNotFoundException(this, raw, targetType);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}