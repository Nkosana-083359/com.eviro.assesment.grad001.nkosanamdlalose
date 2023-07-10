package com.example.envirodemo.service;

import java.io.File;
import java.net.URL;

public interface FileParserService {
    void parseCSV(File csvFile);
    File convertCSVDataToImage(String base64ImageData);
    URL createImageLink(File fileImage);
    void callParseCSV();
}
