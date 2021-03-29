package com.swieghelm.redgnu.config.convert.exception;

import com.google.inject.TypeLiteral;

public class TypeConversionFailure extends TypeConversionException {

    private final Object rawValue;
    private final TypeLiteral<?> targetType;
    private final Exception convertionException;

    public TypeConversionFailure(final Object rawValue,
                                 final TypeLiteral<?> targetType,
                                 final Exception convertionException) {
        super("Type Conversion failure: Cannot convert " + rawValue + " to " + targetType, convertionException);
        this.rawValue = rawValue;
        this.targetType = targetType;
        this.convertionException = convertionException;
    }

    public Object getRawValue() {
        return rawValue;
    }

    public TypeLiteral<?> getTargetType() {
        return targetType;
    }

    public Exception getConvertionException() {
        return convertionException;
    }

}