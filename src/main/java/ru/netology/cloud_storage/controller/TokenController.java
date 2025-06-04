package ru.netology.cloud_storage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloud_storage.service.FileService;

import java.util.List;

@RestController //анотация для обработки HTTP запросов
@RequestMapping("api/cloud") //базовый пусть для всех эдпоинтов
@AllArgsConstructor
public class TokenController {
    private final FileService fileService;

    //эдпоинт для загрузки файлов
    @PostMapping("/file")
    public ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile file,
                                             @RequestParam("filename") String filename,
                                             @RequestHeader("autch-token") String token) {
        return ResponseEntity.ok("Файл загружен" + filename);
    }
    //эдпоинт для получения списка файлов
    @GetMapping("/list")
    public ResponseEntity<List<String>> listOfFiles(@RequestParam("autch-token") String token){
       List<String> list = fileService.listOfFiles(token);
       return ResponseEntity.ok(list);
    }
}
