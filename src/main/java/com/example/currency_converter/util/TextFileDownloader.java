package com.example.currency_converter.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


@Component
public class TextFileDownloader {


    @Value("${url}")
    private String url;


    /**
     * Download file from URL in param to TXT file - DownloadedFile_1.txt.
     * If txt file not exist - create such file.
     *
     * @return
     */
    public File getFile() {
        File file = new File("DownloadedFile_1.txt");
        try {
            URL website = new URL(this.url);
            try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                 FileOutputStream fos = new FileOutputStream(file)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
