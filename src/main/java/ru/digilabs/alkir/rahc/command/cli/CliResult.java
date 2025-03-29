package ru.digilabs.alkir.rahc.command.cli;

import jakarta.annotation.Nullable;

public record CliResult(boolean success, @Nullable String errorMessage, Object data) {
    public CliResult {
        if (!success && errorMessage == null) {
            throw new IllegalArgumentException("errorMessage must be not null when success is false");
        }
    }

    public static CliResult success(Object data) {
        return new CliResult(true, null, data);
    }

    public static CliResult error(String errorMessage, Throwable e) {
        return new CliResult(false, errorMessage, e);
    }
}
