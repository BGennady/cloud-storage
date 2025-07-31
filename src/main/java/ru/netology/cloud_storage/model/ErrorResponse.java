package ru.netology.cloud_storage.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int id;

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}
