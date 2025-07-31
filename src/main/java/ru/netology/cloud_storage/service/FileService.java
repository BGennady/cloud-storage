package ru.netology.cloud_storage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.model.Files;
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

//    private final TokenRepository tokenRepository;
    private final FilesRepository filesRepository;
    private final UserService userService;

    //метод для сохранения файла
    public void fileUpload(String filename, MultipartFile multipartFile) {

        // получение текущего авторизованого пользователя из Spring Seqcurity
        User user = userService.getCurrentUser();

        // путь к сохранению файла
        String saveFile = "D:/New/" + filename;

        try {
            //сохранение файла на диск
            multipartFile.transferTo(new File(saveFile));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл", e);
        }

        //создать запись в БД
        Files file = new Files() //новая сущность Files, и в нее передаю
                .setFilename(filename) //имя файла
                .setPath(saveFile) //куда его сохранить
                .setSize((int) multipartFile.getSize()) //его размер
                .setUploadTime(LocalDateTime.now()) //дата/время
                .setUser(user); //имя пользователя

        filesRepository.save(file);
    }

    // метод для получения списка файлов
    public List<String> listOfFiles() {

        // получение текущего авторизованого пользователя
        User user = userService.getCurrentUser();

        Long userId = user.getId();

        //получаю список записей о файлах пользователя в виде потока
        List<Files> allFiles = filesRepository.findAllByUser_Id(userId);

        //перевожу список файлов в список их имен
        List<String> filesName = allFiles.stream()
                .map(Files::getFilename) //вынимает имя из Files и преобразует в String fileName
                .toList(); //положить в List

        return filesName;
    }

    // метод удаления файлов
    public void fileDelete(String fileName) {

        // получение текущего авторизованого пользователя
        User user = userService.getCurrentUser();

        Long userId = user.getId();

        //нахожу в БД нужный файл
        Optional<Files> fileOpt = filesRepository.findByUser_IdAndFilename(userId, fileName);
        if (fileOpt.isPresent()) {

            //удаляю из БД
            Files file = fileOpt.get();
            filesRepository.delete(file);

            //удаляю с диска
            File diskFile = new File(file.getPath());
            if (diskFile.exists()) {
                boolean deleted = diskFile.delete();
                if (!deleted) {
                    throw new RuntimeException("Файл не был удален");
                }
            } else {
                throw new RuntimeException("Файл не найде");
            }
        }
    }
}