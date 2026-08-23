package ru.practicum.main.exception;

public record ApiError(
        String status, String reason, String message, String timestamp
) {
}
