package com.imstargg.admin.support.error;

public class AdminException extends RuntimeException {

    private final AdminErrorKind errorKind;

    public AdminException(AdminErrorKind errorKind, String message) {
        super(message);
        this.errorKind = errorKind;
    }

    public AdminException(AdminErrorKind errorKind, String message, Exception e) {
        super(message, e);
        this.errorKind = errorKind;
    }

    public AdminErrorKind getErrorKind() {
        return errorKind;
    }
}
