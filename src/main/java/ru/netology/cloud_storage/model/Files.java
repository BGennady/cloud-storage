package ru.netology.cloud_storage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "files", schema = "storage")

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Files {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, unique = true)
    private String filename;

    @Column(nullable = false)
    private String path;

    private int size;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    @ManyToOne
    @JoinColumn (name = "user_id")
    User user;
}
