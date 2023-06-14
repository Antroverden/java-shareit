package ru.practicum.shareit.exception;

public class BadRequestException {

    private final String error;

    public BadRequestException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
