package com.imstargg.core.api.test;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public final class RestDocsAttribute {

    public static final String CONSTRAINT_KEY = "constraint";
    public static final String TYPE_KEY = "type";
    public static final String DEFAULT_VALUE_KEY = "defaultValue";

    public static Attributes.Attribute constraint(String value) {
        return key(CONSTRAINT_KEY).value(value);
    }

    public static Attributes.Attribute type(Class<?> clazz) {
        return key(TYPE_KEY).value(clazz.getSimpleName());
    }

    public static Attributes.Attribute defaultValue(String value) {
        return key(DEFAULT_VALUE_KEY).value(value);
    }

    public static Attributes.Attribute defaultValue(int value) {
        return key(DEFAULT_VALUE_KEY).value(value);
    }

    private RestDocsAttribute() {
        throw new UnsupportedOperationException();
    }
}
