package com.example.envirodemo.controller;

import com.example.envirodemo.repository.AccountProfileRepository;
import com.example.envirodemo.service.impl.FileParserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URL;

@RestController
@RequestMapping("/v1/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final AccountProfileRepository accountProfileRepository;
    private final FileParserServiceImpl fileParserService;

    @PostMapping(value = "/parse/{file}")
    public void parseCSV(@RequestBody File file) {
        File csvFile = new File(file.getAbsolutePath());
        fileParserService.parseCSV(csvFile);
        System.out.println("Parsed File Succesfully");
    }
    @GetMapping(value = "/{name}/{surname}/{\\w\\.\\w}")
    public FileSystemResource getHttpImageLink(@PathVariable String name, @PathVariable String surname){

        var accountProfile = accountProfileRepository.getByNameAndSurname(name, surname);
        if (accountProfile != null) {


            return new FileSystemResource(accountProfile.getHttpImageLink());
        } else {
            return null;
        }
    }
    }

