package ru.netology.cloud_storage.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.model.Files;
import ru.netology.cloud_storage.model.Token;
import ru.netology.cloud_storage.model.User;
import ru.netology.cloud_storage.repository.FilesRepository;
import ru.netology.cloud_storage.repository.TokenRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    private FileService fileService; //тестируемый сервис
    private TokenRepository tokenRepository; //мок репозитория токенов
    private FilesRepository filesRepository; //мок репозитория файлов
    final private String fileName = "test_file.txt"; //имя файла
    final private String path = "uploads/" + fileName; //его путь
    final private String token = "a12345b";
    final private Long userId = 101L;

    private User testUser;
    private Token testToken;


    @BeforeEach
    void init() {
        tokenRepository = mock(TokenRepository.class);
        filesRepository = mock(FilesRepository.class);
        fileService = new FileService(tokenRepository, filesRepository);

        testUser = new User();
        testUser.setId(userId);
        testToken = new Token();
        testToken.setToken(token);
        testToken.setUser(testUser);
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

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(testToken));
        when(mockFile.getSize()).thenReturn(500L);
        doNothing().when(mockFile).transferTo(any(File.class));

        fileService.fileUpload(fileName, mockFile, token);
        ArgumentCaptor<Files> captor = ArgumentCaptor.forClass(Files.class);
        verify(filesRepository).save(captor.capture());
        Files saveFile = captor.getValue();

        assertEquals(fileName, saveFile.getFilename());
        assertEquals(500, saveFile.getSize());
    }

    @Test
    public void listOfFilesTest() {

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(testToken));

        Files file1 = new Files();
        file1.setFilename("test1.txt");
        Files file2 = new Files();
        file2.setFilename("test2.txt");

        when(filesRepository.findAllByUser_Id(userId)).thenReturn(List.of(file1, file2));

        List<String> result = fileService.listOfFiles(token);

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

        //файл в БД (проверка удаления)
        Files fileInDB = new Files();
        fileInDB.setFilename(fileName);
        fileInDB.setPath(path);
        fileInDB.setUser(testToken.getUser());

        //моки на репозитории
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(testToken));
        when(filesRepository.findByUser_IdAndFilename(userId, fileName)).thenReturn(Optional.of(fileInDB));

        //метод удаления
        fileService.fileDelete(fileName, token);

        //проверка, что файл удален из БД
        verify(filesRepository).delete(fileInDB);

        //проверка, что файл удален c диска
        assertEquals(false, fileOnDisk.exists());

    }
}
