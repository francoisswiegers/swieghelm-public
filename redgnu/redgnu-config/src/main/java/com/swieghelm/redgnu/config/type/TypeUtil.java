package com.swieghelm.redgnu.config.type;

import com.google.inject.TypeLiteral;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public final class TypeUtil {

    public static boolean isParameterized(final TypeLiteral<?> typeLiteral) {
        final Type type = typeLiteral.getType();
        return type instanceof ParameterizedType;
    }

    public static List<TypeLiteral<?>> getParameterizedTypes(final TypeLiteral<?> typeLiteral) {
        if (!isParameterized(typeLiteral)) {
            return emptyList();
        }
        final ParameterizedType parameterizedType = (ParameterizedType) typeLiteral.getType();
        final Type[] parameterTypes = parameterizedType.getActualTypeArguments();
        if (parameterTypes == null) {
            return emptyList();
        }
        return Arrays.stream(parameterTypes)
                     .map((Function<Type, ? extends TypeLiteral<?>>) TypeLiteral::get)
                     .collect(toList());
    }

}