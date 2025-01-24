package com.imstargg.core.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ObjectMapperHelper {

    private final ObjectMapper objectMapper;

    public ObjectMapperHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String write(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (IOException e) {
            throw new CoreException("Failed to write value as string", e);
        }
    }

    public <T> T read(String value, Class<T> valueType) {
        try {
            return objectMapper.readValue(value, valueType);
        } catch (IOException e) {
            throw new CoreException("Failed to read value", e);
        }
    }

    public <T> T read(String value, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(value, typeReference);
        } catch (IOException e) {
            throw new CoreException("Failed to read value", e);
        }
    }
}
