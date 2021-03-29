package com.swieghelm.redgnu.config.convert;

import com.google.inject.TypeLiteral;

public interface TypeConversion {

    Object convert(Object raw, TypeLiteral<?> targetType);

}