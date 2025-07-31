package ru.netology.cloud_storage.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.model.Files;
import ru.netology.cloud_storage.model.User;
import ru.netology.cloud_storage.repository.FilesRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    private FileService fileService; //тестируемый сервис
    private FilesRepository filesRepository; //мок репозитория файлов
    private UserService userService; //мок UserService;

    final private String fileName = "test_file.txt"; //имя файла
    final private String path = "D:/New/" + fileName; //его путь

    private User testUser;

    @BeforeEach
    void init() {
        filesRepository = mock(FilesRepository.class);
        userService = mock(UserService.class);
        fileService = new FileService(filesRepository, userService);

        testUser = new User(); //тестовый пользователь
        testUser.setId(101L); //его ID

        when(userService.getCurrentUser()).thenReturn(testUser); //при вызове UserService пепедать тестовго user;
    }

    @AfterEach
    void clean() {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    public void fileUploadTest() throws IOException {

        MultipartFile mockFile = mock(MultipartFile.class);

        when(mockFile.getSize()).thenReturn(500L); //передача объема файла
        doNothing().when(mockFile).transferTo(any(File.class)); //имитация его сохранения

        fileService.fileUpload(fileName, mockFile); //вызов метода

        //перхватывает, то что было переданотв в Save
        ArgumentCaptor<Files> captor = ArgumentCaptor.forClass(Files.class);
        verify(filesRepository).save(captor.capture());
        Files saveFile = captor.getValue();

        // сравниваем имя и размер
        assertEquals(fileName, saveFile.getFilename());
        assertEquals(500, saveFile.getSize());
    }

    @Test
    public void listOfFilesTest() {

        // два файла для возврата списка
        Files file1 = new Files().setFilename("test1.txt");
        Files file2 = new Files().setFilename("test2.txt");

        when(filesRepository.findAllByUser_Id(testUser.getId())).thenReturn(List.of(file1, file2));

        List<String> result = fileService.listOfFiles();

        assertEquals(2, result.size());
        assertEquals("test1.txt", result.get(0));
        assertEquals("test2.txt", result.get(1));
    }

    @Test
    public void fileDeleteTest() {
        //файл на диске (проверка удаления)
        File fileOnDisk = new File(path);

        try {
            //создание папки
            fileOnDisk.getParentFile().mkdirs();
            //создание файла
            fileOnDisk.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("не удалось создать тестовый файл", e);
        }

        // файл в БД для проверки удаления
        Files fileInDB = new Files()
                .setFilename(fileName)
                .setPath(path)
                .setUser(testUser);

        //моки на репозитории
        when(filesRepository.findByUser_IdAndFilename(testUser.getId(), fileName)).thenReturn(Optional.of(fileInDB));

        //метод удаления
        fileService.fileDelete(fileName);

        //проверка, что файл удален из БД
        verify(filesRepository).delete(fileInDB);

        //проверка, что файл удален c диска
        assertEquals(false, fileOnDisk.exists());

    }
}
