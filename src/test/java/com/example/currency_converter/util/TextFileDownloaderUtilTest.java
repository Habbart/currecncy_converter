package com.example.currency_converter.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class TextFileDownloaderUtilTest {


    private final String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";


    @Test
    void getFile() {

        File file = TextFileDownloaderUtil.getFile(url);

        assertTrue(file.exists(), "File should be created");
        assertNotEquals(0, file.length(), "File shouldn't be empty");
        assertNotEquals("", file.getName(), "File name should present");

        file.delete();
    }
}