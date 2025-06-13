package ru.netology.cloud_storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "users", schema = "storage")

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id; //порядковый номер в бд

    @Column(nullable = false, unique = true)
    private String login; //логин подльзователя

    @Column(nullable = false)
    private String password; //пароль пользователя
}