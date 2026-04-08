package br.com.pedrolourenco.todoapp.domain.task;

import java.time.LocalDate;

public record Task(
        String title,
        String description,
        LocalDate createdAt,
        LocalDate dueDate
) {
    public Task(String title, String description, LocalDate expirationDate) {
        this(title, description, LocalDate.now(), expirationDate);
    }
}
