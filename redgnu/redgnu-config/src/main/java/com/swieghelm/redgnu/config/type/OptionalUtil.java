package com.swieghelm.redgnu.config.type;

import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.ConfigException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
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
        final Type type = typeLiteral.getType();
        if (!(type instanceof ParameterizedType)) {
            throw new ConfigException("Optional type " + typeLiteral + " is not parameterized");
        }
        final ParameterizedType parameterizedType = (ParameterizedType) type;
        final Type[] parameterTypes = parameterizedType.getActualTypeArguments();
        if (parameterTypes == null || parameterTypes.length != 1) {
            throw new ConfigException("Optional type " + typeLiteral + " does not have a single wrapped type");
        }
        final Type wrappedType = parameterTypes[0];
        if (wrappedType instanceof WildcardType) {
            throw new ConfigException("Wildcard Optional types not supported");
        }
        return TypeLiteral.get(wrappedType);
    }

}
