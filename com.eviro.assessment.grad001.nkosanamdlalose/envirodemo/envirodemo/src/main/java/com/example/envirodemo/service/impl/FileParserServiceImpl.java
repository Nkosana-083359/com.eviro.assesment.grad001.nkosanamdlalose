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



    private static final String DB_URL = "jdbc:h2:mem:testdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "1234";

    @Override
    public void parseCSV(File csvFile) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO ACCOUNT_PROFILE  (NAME, SURNAME, HTTP_IMAGE_LINK) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            Files.lines(Paths.get(csvFile.getAbsolutePath()))
                    .skip(1) // skip header row
                    .map(line -> line.split(","))
                    .forEach(data -> {
                        try {
                            String name = data[0];
                            String surname = data[1];
                            String imageFormat = data[2];
                            String base64ImageData = data[3];
                            //byte[] imageBytes = Base64.getDecoder().decode(imageData);


                            String httpImageLink = convertCSVDataToImage(base64ImageData).toURI().toURL().toString();

                            pstmt.setString(1, name);
                            pstmt.setString(2, surname);
                            pstmt.setString(3, httpImageLink);

                            pstmt.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (SQLException | IOException e) {
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
            return new URL("http://localhost:8090/v1/api/image/" + fileImage.getName());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
