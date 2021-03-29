package com.swieghelm.redgnu.config.convert.exception;

import com.swieghelm.redgnu.config.ConfigException;

public class TypeConversionException extends ConfigException {

    public TypeConversionException(String message) {
        super(message);
    }

    public TypeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}