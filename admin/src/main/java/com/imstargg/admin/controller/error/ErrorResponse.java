package com.imstargg.admin.controller.error;


import com.imstargg.admin.support.error.AdminErrorKind;

public record ErrorResponse(
        AdminErrorKind type,
        String message
) {
}
