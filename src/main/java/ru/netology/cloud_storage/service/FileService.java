package ru.netology.cloud_storage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.model.Files;
import ru.netology.cloud_storage.model.Token;
import ru.netology.cloud_storage.model.User;
import ru.netology.cloud_storage.repository.FilesRepository;
import ru.netology.cloud_storage.repository.TokenRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileService {

    private final TokenRepository tokenRepository;
    private final FilesRepository filesRepository;

    //метод для сохранения файла
    public void fileUpload(String filename, MultipartFile multipartFile, String token) {
        // ищем token в БД
        Optional<Token> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Токен не найден");
        }
        Token userToken = tokenOpt.get();

        // путь к сохранению файла
        String saveFile = "uploads/" + filename;

        try {
            //сохранение файла на диск
            multipartFile.transferTo(new File(saveFile));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл", e);
        }

        //создать запись в БД
        Files file = new Files(); //новая сущность Files, и в нее передаю
        file.setFilename(filename); //имя файла
        file.setPath(saveFile); //куда его сохранить
        file.setSize((int) multipartFile.getSize()); //его размер
        file.setUploadTime(LocalDateTime.now()); //дата/время
        file.setUser(userToken.getUser()); //имя пользователя

        filesRepository.save(file);
    }
    public List<String> listOfFiles (String token){
        Optional<Token> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Токен не найден");
        }
        Long userId = tokenOpt.get().getUser().getId();

        //получаю список записей о файлах пользователя в виде потока
        List<Files> allFiles = filesRepository.findAllByUser_Id(userId);

        //перевожу список файлов в список их имен
        List<String> filesName = allFiles.stream()
                .map(Files::getFilename) //вынимает имя из Files и преобразует в String fileName
                .toList(); //положить в List

        return filesName;
    }
}
