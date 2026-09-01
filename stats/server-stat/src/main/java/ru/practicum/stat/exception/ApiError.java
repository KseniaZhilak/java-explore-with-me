package ru.practicum.stat.exception;

public record ApiError(
        String status, String reason, String message, String timestamp
) {
}
