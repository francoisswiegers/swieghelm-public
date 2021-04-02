package com.swieghelm.redgnu.config.type;

import com.google.inject.TypeLiteral;

import java.util.Optional;

public final class OptionalUtil {

    private OptionalUtil() {
    }

    public static boolean isOptional(final TypeLiteral<?> typeLiteral) {
        return Optional.class.equals(typeLiteral.getRawType());
    }

    public static TypeLiteral<?> getConversionType(final TypeLiteral<?> typeLiteral) {
        return isOptional(typeLiteral) ? unwrapOptional(typeLiteral) : typeLiteral;
    }

    private static TypeLiteral<?> unwrapOptional(final TypeLiteral<?> typeLiteral) {
        return TypeUtil.getParameterizedTypes(typeLiteral).get(0);
    }

}
