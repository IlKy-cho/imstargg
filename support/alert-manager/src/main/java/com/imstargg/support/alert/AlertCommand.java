package com.imstargg.support.alert;

import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;

public record AlertCommand(
        String title,
        @Nullable String content,
        @Nullable Exception ex
) {

    public static AlertCommandBuilder builder() {
        return new AlertCommandBuilder();
    }

    @Nullable
    public String exStackTrace() {
        if (ex == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

}
