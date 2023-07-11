package com.example.envirodemo.controller;

import com.example.envirodemo.repository.AccountProfileRepository;
import com.example.envirodemo.service.impl.FileParserServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/v1/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final AccountProfileRepository accountProfileRepository;
    private final FileParserServiceImpl fileParserService;


    @GetMapping(value = "/{name}/{surname}/{\\w\\.\\w}")
    public FileSystemResource getHttpImageLink(@PathVariable String name, @PathVariable String surname){
        fileParserService.callParseCSV();
        var accountProfile = accountProfileRepository.getByNameAndSurname(name, surname);
        if (accountProfile != null) {
return new FileSystemResource(accountProfile.getHttpImageLink().substring(22));

        } else {
            return null;
        }
    }
    }

