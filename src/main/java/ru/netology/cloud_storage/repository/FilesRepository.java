package ru.netology.cloud_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloud_storage.model.Files;

import java.util.List;
import java.util.Optional;

// репозиторий для связи приложения с БД через JPA
@Repository
public interface FilesRepository extends JpaRepository <Files, Long>{
    List<Files> findAllByUser_Id (Long userId);
    Optional<Files> findByUser_IdAndFilename (Long userId, String fileName);
}