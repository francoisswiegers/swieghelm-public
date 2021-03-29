package com.swieghelm.redgnu.config.extension.vanilla;

public class DefaultConstants {

    public static final String UNDEFINED = "#UNDEFINED";

    public static String valueOrUndefined(final String value) {
        return UNDEFINED.equals(value) ? null : value;
    }

}
