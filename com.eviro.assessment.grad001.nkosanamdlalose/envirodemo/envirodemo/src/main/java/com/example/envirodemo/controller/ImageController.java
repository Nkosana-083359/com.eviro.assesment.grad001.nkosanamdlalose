package com.example.envirodemo.controller;

import com.example.envirodemo.repository.AccountProfileRepository;
import com.example.envirodemo.service.impl.FileParserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/v1/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final AccountProfileRepository accountProfileRepository;
    private final FileParserServiceImpl fileParserService;


    @GetMapping(value = "/{name}/{surname}")
    public FileSystemResource getHttpImageLink(@PathVariable String name, @PathVariable String surname){
        File file = new File("C:\\Users\\lenovo\\Documents\\Resume\\1672815113084-GraduateDev_AssessmentCsv_Ref003.csv");
        fileParserService.parseCSV(file);
        var accountProfile = accountProfileRepository.getByNameAndSurname(name, surname);
        if (accountProfile != null) {

            File imageFile = new File(accountProfile.getHttpImageLink().substring(5));
            return new FileSystemResource(imageFile);
        } else {
            return null;
        }
    }
    }

