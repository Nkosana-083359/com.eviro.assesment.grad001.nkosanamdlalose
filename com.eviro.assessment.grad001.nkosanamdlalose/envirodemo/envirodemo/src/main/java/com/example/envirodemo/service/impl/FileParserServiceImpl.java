package com.example.envirodemo.service.impl;

import com.example.envirodemo.account.csv.CsvRecord;
import com.example.envirodemo.entity.AccountProfile;
import com.example.envirodemo.repository.AccountProfileRepository;
import com.example.envirodemo.service.FileParserService;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;


@Service
public class FileParserServiceImpl implements FileParserService {

    @Autowired
    private AccountProfileRepository accountProfileRepository;

    @Override
    public void parseCSV(File csvFile) {
        try (Reader reader = new FileReader(csvFile)) {
            CsvToBean<CsvRecord> csvToBean = new CsvToBeanBuilder<CsvRecord>(reader)
                    .withType(CsvRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<CsvRecord> records = csvToBean.parse();

            for (CsvRecord record : records) {
                String name = record.getName();
                String surname = record.getSurname();
                String imageFormat = record.getImageFormat();
                String base64ImageData = record.getImageData();

                File imageFile = convertCSVDataToImage(base64ImageData);

                String httpImageLink = createImageLink(imageFile).toString();

                AccountProfile accountProfile = new AccountProfile();
                accountProfile.setName(name);
                accountProfile.setSurname(surname);
                accountProfile.setHttpImageLink(httpImageLink);

                accountProfileRepository.save(accountProfile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File convertCSVDataToImage(String base64ImageData) {
        byte[] imageData = Base64.getDecoder().decode(base64ImageData);
        Path imagePath = Paths.get("image.jpg");
        try {
            Files.write(imagePath, imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath.toFile();
    }

    @Override
    public URL createImageLink(File fileImage) {
        try {
            return new URL("file://" + fileImage.getAbsolutePath());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
