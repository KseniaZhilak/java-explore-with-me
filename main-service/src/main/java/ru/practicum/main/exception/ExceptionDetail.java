package ru.practicum.main.exception;

public record ExceptionDetail(
        String status, String reason, String message, String timestamp
) {
}
