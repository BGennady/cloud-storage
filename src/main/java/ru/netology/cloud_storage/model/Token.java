package ru.netology.cloud_storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "token", schema = "storage")

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id; //порядковый номер в БД>

    private String token; //токен

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; //свзять много (токенов) к 1му (пользователю)

    @Column(name = "created_at")
    private LocalDateTime createdAt; //дата и время создания токена
}