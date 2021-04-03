package com.swieghelm.redgnu.config.convert.exception;

import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.convert.TypeConversion;

public class ConversionNotFoundException extends TypeConversionException {

    private final TypeConversion typeConversion;
    private final Object rawValue;
    private final TypeLiteral<?> targetType;

    public ConversionNotFoundException(final TypeConversion typeConversion,
                                       final Object rawValue,
                                       final TypeLiteral<?> targetType) {
        super("Could not find a conversion for " + rawValue + " to " + targetType + " in " + typeConversion);
        this.typeConversion = typeConversion;
        this.rawValue = rawValue;
        this.targetType = targetType;
    }

    public TypeConversion getTypeConversion() {
        return typeConversion;
    }

    public Object getRawValue() {
        return rawValue;
    }

    public TypeLiteral<?> getTargetType() {
        return targetType;
    }
}