package ru.netology.cloud_storage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.service.FileService;

import java.util.List;

@RestController //анотация для обработки HTTP запросов
@RequestMapping("/cloud") //базовый пусть для всех эдпоинтов
@AllArgsConstructor
public class FilesController {
    private final FileService fileService;

    //эдпоинт для загрузки файлов
    @PostMapping("/file")
    public ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile file,
                                             @RequestParam("filename") String filename) {
        fileService.fileUpload(filename,file);
        return ResponseEntity.ok("Файл загружен" + filename);
    }
    //эдпоинт для получения списка файлов
    @GetMapping("/list")
    public ResponseEntity<List<String>> listOfFiles(){
        List<String> list = fileService.listOfFiles();
        return ResponseEntity.ok(list);
    }

    //эдпоинт для удаления файла
    @DeleteMapping("/delete")
    public ResponseEntity<String> fileDelite (@RequestParam("filename") String filename){
        fileService.fileDelete(filename);
        return ResponseEntity.ok("Файл удален" + filename);
    }
}