package com.imstargg.admin.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record NewPlayerRequest(
        @NotNull @Pattern(regexp = "#[A-Z0-9]+") String tag
) {
}
