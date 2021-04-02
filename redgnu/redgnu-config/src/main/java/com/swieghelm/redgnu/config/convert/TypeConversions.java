package com.swieghelm.redgnu.config.convert;

import com.google.common.base.Verify;
import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.convert.exception.ConversionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Verify.verifyNotNull;

/**
 * Evaluate multiple type conversions in order. First to resolve wins, so put preferred convertions higher in the list.
 */
public class TypeConversions implements TypeConversion {

    private static final Logger LOG = LoggerFactory.getLogger(TypeConversions.class);

    private final List<TypeConversion> conversions;

    public TypeConversions(final TypeConversion... conversions) {
        this(Arrays.asList(conversions));
    }

    public TypeConversions(final List<TypeConversion> conversions) {
        this.conversions = new ArrayList<>(verifyNotNull(conversions, "Conversions list cannot be null"));
    }

    @Override
    public Object convert(final Object raw, final TypeLiteral<?> targetType) {
        for (final TypeConversion typeConversion : conversions) {
            try {
                return typeConversion.convert(raw, targetType);
            }
            catch (final ConversionNotFoundException e) {
                LOG.debug("{} did not contain a conversion for {} to {}", typeConversion, raw, targetType);
            }
        }
        LOG.debug("No type conversion for {} to {} found in any of {}", raw, targetType, conversions);
        throw new ConversionNotFoundException(this, raw, targetType);
    }
}