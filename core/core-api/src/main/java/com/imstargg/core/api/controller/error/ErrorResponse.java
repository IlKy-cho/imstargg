package com.imstargg.core.api.controller.error;


import com.imstargg.core.domain.CoreErrorType;

public record ErrorResponse(
        CoreErrorType type,
        String message
) {

    public static ErrorResponse of(CoreErrorType type) {
        return new ErrorResponse(type, type.getMessage());
    }

}
